package com.najib.ecommerce.view.adapter

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.App
import com.najib.ecommerce.R
import com.najib.ecommerce.api.util.OnResponse
import com.najib.ecommerce.util.Functions
import kotlinx.android.synthetic.main.base_adapter_recycler_empty.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_error.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_error_pagination.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_loading.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_loading_pagination.view.*

abstract class CoreRecyclerViewAdapter<DATA, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var actualDataList: MutableList<DATA> = ArrayList()

    var emptyText: String? = null
    var loadingText: String? = null
    var errorText: String? = null

    private var state: Int = NORMAL

    private var recyclerView: RecyclerView? = null

    private var filteredDataList: MutableList<DATA> = ArrayList()

    var retryListener: OnRetryListener? = null

    private var filterTask: FilterTask<DATA>? = null

    private var filterer: DataListFilter<DATA>? = null

    private var diffUtilNotifier: DiffUtilNotifier<DATA>? = null

    var isRestoringRecyclerState = false

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    private var hasExtraRow = true
        set(value) {
            val isChanged = (field != value)
            val hadExtraRow = field
            field = value
            if (isChanged) {
                if (hadExtraRow) notifyItemRemoved(itemCount)
                else notifyItemInserted(itemCount)
            } else {
                notifyItemChanged(itemCount - 1)
            }
            recyclerView?.layoutManager?.let {
                handleExtraRowLayoutManager(it)
            }
        }

    fun setState(resource: OnResponse<*>?) {
        when (resource?.status) {
            OnResponse.LOADING -> {
                setState(LOADING)
            }
            OnResponse.ERROR -> {
                errorText = resource.error.toString()
                setState(ERROR)
            }
            OnResponse.SUCCESS -> {
                setState(NORMAL)
            }
        }
    }

    fun setState(state: Int) {
        val previousState = this.state
        this.state = state
        if (hasExtraRow && previousState != state) notifyItemChanged(itemCount - 1)
        recyclerView?.layoutManager?.let {
            handleExtraRowLayoutManager(it)
        }
    }

    fun handleExtraRowLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val size = getDataList().size
                    return if (position >= size) layoutManager.spanCount
                    else 1
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow && position == itemCount - 1) {
            if (state == LOADING) if (getDataList().isEmpty()) LAYOUT_LOADING else LAYOUT_LOADING_PAGINATION
            else if (state == ERROR) if (getDataList().isEmpty()) LAYOUT_ERROR else LAYOUT_ERROR_PAGINATION
            else if (getDataList().isEmpty()) LAYOUT_EMPTY else LAYOUT_NULL
        } else {
            getDataItemViewType(position)
        }
    }

    open fun getDataItemViewType(position: Int): Int {
        return LAYOUT_DATA
    }

    fun setDataList(list: Collection<DATA>?, listener: OnDataListFilterListener? = null) {
        actualDataList.clear()
        if (list != null) actualDataList.addAll(list)
        filterDataList(listener)
    }

    /*fun clearDataList() {
        try {
            if (actualDataList.isNotEmpty()) actualDataList.clear()
            if (filteredDataList.isNotEmpty()) filteredDataList.clear()
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }*/

    fun clearDataList() {
        try {
            if (filteredDataList.size > 0) filteredDataList.clear()
            notifyDataSetChanged()
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    /*fun setDataList(list: Collection<DATA>?, listener: OnDataListFilterListenerImpl) {
        setDataList(list, object : OnDataListFilterListener {
            override fun onDataListFilteringCompleted() {
                listener.invoke()
            }
        })
    }*/

    private fun filterDataList(listener: OnDataListFilterListener? = null) {
        filterTask?.cancel(true)
        val oldList = ArrayList(filteredDataList)

        if (filterer == null) {
            //            filteredDataList.clear()
            filteredDataList.addAll(actualDataList)
            if (recyclerView != null && oldList.isEmpty()) {
                if (!isRestoringRecyclerState) notifyItemRangeChanged(0, filteredDataList.size)
            } else {
                notifyDataSetDiffUtil(oldList, filteredDataList)
            }
            listener?.onDataListFilteringCompleted()
        } else {
            filterTask = FilterTask(actualDataList, filterer, object : FilterTaskListener<DATA> {
                override fun onFilterTaskListenerCompleted(
                    filter: DataListFilter<DATA>?,
                    dataList: List<DATA>
                ) {
                    filteredDataList.clear()
                    filteredDataList.addAll(dataList)
                    if (recyclerView != null && oldList.isEmpty()) {
                        if (!isRestoringRecyclerState) notifyItemRangeChanged(
                            0,
                            filteredDataList.size
                        )
                    } else {
                        notifyDataSetDiffUtil(oldList, filteredDataList)
                    }
                    listener?.onDataListFilteringCompleted()
                }
            })
            filterTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_NULL -> onCreateNullViewHolder(parent)
            LAYOUT_EMPTY -> onCreateEmptyViewHolder(parent)
            LAYOUT_LOADING -> onCreateLoadingViewHolder(parent)
            LAYOUT_LOADING_PAGINATION -> onCreateLoadingPaginationViewHolder(parent)
            LAYOUT_ERROR -> onCreateErrorViewHolder(parent)
            LAYOUT_ERROR_PAGINATION -> onCreateErrorPaginationViewHolder(parent)
            else -> onCreateDataViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        return filteredDataList.size + if (hasExtraRow) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            LAYOUT_NULL -> onBindNullViewHolder(holder)
            LAYOUT_EMPTY -> onBindEmptyViewHolder(holder)
            LAYOUT_LOADING -> onBindLoadingViewHolder(holder)
            LAYOUT_LOADING_PAGINATION -> onBindLoadingPaginationViewHolder(holder)
            LAYOUT_ERROR -> onBindErrorViewHolder(holder)
            LAYOUT_ERROR_PAGINATION -> onBindErrorPaginationViewHolder(holder)
            else -> onBindDataViewHolder(holder as VH, viewType)
        }
    }

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    open fun onCreateNullViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NullViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_null))
    }

    open fun onCreateEmptyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return EmptyViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_empty))
    }

    open fun onCreateLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_loading))
    }

    open fun onCreateLoadingPaginationViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingPaginationViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_loading_pagination))
    }

    open fun onCreateErrorViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ErrorViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_error))
    }

    open fun onCreateErrorPaginationViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ErrorPaginationViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_error_pagination))
    }


    inner class NullViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            // Do nothing
        }
    }

    inner class EmptyViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            if (emptyText.isNullOrEmpty()) emptyText =
                App.context.resources.getString(R.string.empty)
            itemView.txt_empty.text = emptyText
        }
    }

    inner class LoadingViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            itemView.txt_loading.setText(loadingText)

            //            if (loadingText.isNullOrEmpty()) itemView.txt_loading.visibility = View.GONE
            //            else itemView.txt_loading.visibility = View.VISIBLE

            itemView.txt_loading.visibility = View.VISIBLE
        }
    }

    inner class LoadingPaginationViewHolder<T, U>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            itemView.txt_loading_pagination.setText(loadingText)
            if (loadingText.isNullOrEmpty()) itemView.txt_loading_pagination.visibility = View.GONE
            else itemView.txt_loading_pagination.visibility = View.VISIBLE
        }
    }

    inner class ErrorViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.btn_retry.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (view == itemView.btn_retry) retryListener?.onRetryClicked()
        }

        fun bindView() {
            itemView.txt_error.text = errorText
            if (errorText.isNullOrEmpty()) itemView.txt_error.visibility = View.GONE
            else itemView.txt_error.visibility = View.VISIBLE
        }
    }

    inner class ErrorPaginationViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (view == itemView) retryListener?.onRetryClicked()
        }

        fun bindView() {
            itemView.txt_error_pagination.text = errorText
            if (errorText.isNullOrEmpty()) itemView.txt_error_pagination.visibility = View.GONE
            else itemView.txt_error_pagination.visibility = View.VISIBLE
        }
    }


    @Suppress("UNCHECKED_CAST")
    open fun onBindNullViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as NullViewHolder<*, *>).bindView()
    }

    @Suppress("UNCHECKED_CAST")
    open fun onBindEmptyViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as EmptyViewHolder<*, *>).bindView()
    }

    @Suppress("UNCHECKED_CAST")
    open fun onBindLoadingViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as LoadingViewHolder<*, *>).bindView()
    }

    /** Binds the [LoadingPaginationViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindLoadingPaginationViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as LoadingPaginationViewHolder<*, *>).bindView()
    }

    /** Binds the [ErrorFullViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindErrorViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as ErrorViewHolder<*, *>).bindView()
    }

    /** Binds the [ErrorPaginationViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindErrorPaginationViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as ErrorPaginationViewHolder<*, *>).bindView()
    }

    /** The internal interface for data filtering */
    private interface FilterTaskListener<DATA> {

        /** Called when internal filtering task is completed */
        fun onFilterTaskListenerCompleted(filter: DataListFilter<DATA>?, dataList: List<DATA>)
    }

    fun setOnRetryListener(onRetryClicked: OnRetryClickedImpl) {
        setOnRetryListener(object : OnRetryListener {
            override fun onRetryClicked() {
                onRetryClicked.invoke()
            }
        })
    }

    fun setOnRetryListener(listener: OnRetryListener?) {
        retryListener = listener
    }


    fun getDataList(): MutableList<DATA> {
        return filteredDataList
    }


    interface OnRetryListener {
        fun onRetryClicked()
    }

    /** Notify this adapter on data changes */
    private fun notifyDataSetDiffUtil(oldList: List<DATA>, newList: List<DATA>) {
        if (diffUtilNotifier != null) {
            diffUtilNotifier?.let {
                val result = DiffUtil.calculateDiff(it.generateDiffUtilCallback(oldList, newList))
                result.dispatchUpdatesTo(this)
            }
        } else {
            notifyDataSetChanged()
        }
    }

    abstract fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindDataViewHolder(holder: VH, position: Int)


    /** The interface for data filtering on data set update */
    interface OnDataListFilterListener {

        /** Called if filtering has been done */
        fun onDataListFilteringCompleted()
    }

    /** The interface for data filtering */
    interface DataListFilter<DATA> {

        /** @return true if the item is included in the filter */
        fun performDataListFiltering(data: DATA): Boolean
    }

    /** The interface to generate a [DiffUtil.Callback] */
    interface DiffUtilNotifier<DATA> {

        /** @return the required [DiffUtil.Callback] */
        fun generateDiffUtilCallback(oldList: List<DATA>, newList: List<DATA>): DiffUtil.Callback
    }

    /** The static task to do filtering in background */
    private class FilterTask<DATA>(
        val initialData: List<DATA>,
        val filterer: DataListFilter<DATA>?,
        val taskListener: FilterTaskListener<DATA>
    ) : AsyncTask<String, String, List<DATA>>() {

        private val dataList = ArrayList<DATA>()

        override fun doInBackground(vararg strings: String): List<DATA> {
            for (i in initialData.indices) {
                val data = initialData[i]
                if (filterer?.performDataListFiltering(data) == true) dataList.add(data)
            }
            return dataList
        }

        override fun onPostExecute(data: List<DATA>) {
            super.onPostExecute(data)
            taskListener.onFilterTaskListenerCompleted(filterer, data)
        }
    }

    companion object {

        const val NORMAL = 1000
        const val LOADING = 1001
        const val ERROR = 1002

        private const val LAYOUT_NULL = 2000
        private const val LAYOUT_DATA = 2001
        private const val LAYOUT_EMPTY = 2002
        private const val LAYOUT_LOADING = 2003
        private const val LAYOUT_LOADING_PAGINATION = 2004
        private const val LAYOUT_ERROR = 2005
        private const val LAYOUT_ERROR_PAGINATION = 2006
    }
}

typealias OnRetryClickedImpl = (() -> Unit)