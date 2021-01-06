package com.najib.ecommerce.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.R
import com.najib.ecommerce.util.GlobalHawk
import com.najib.ecommerce.util.Variables
import com.najib.ecommerce.view.activity.MainActivity
import com.najib.ecommerce.view.activity.ProductDetailActivity
import com.najib.ecommerce.view.activity.SearchActivity
import com.najib.ecommerce.view.adapter.CategoryAdapter
import com.najib.ecommerce.view.adapter.ProductAdapter
import com.najib.ecommerce.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val adapterCategory = CategoryAdapter()
    private val adapterProduct = ProductAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        onClick()
    }

    private fun initView() {
        initRecyclerCategory()
        initRecyclerProduct()
    }

    private fun initViewModel() {
        Handler().postDelayed({
            viewModel.getData()
        }, 200)
        viewModel.onResponse.observe(this, {
            adapterCategory.setState(it)
            adapterProduct.setState(it)
        })
        viewModel.liveData.observe(this, { data ->
            adapterCategory.clearDataList()
            adapterCategory.setDataList(data.category)
            adapterProduct.clearDataList()
            adapterProduct.setDataList(data.productPromo)

            GlobalHawk.setProduct(data.productPromo)
        })
    }

    private fun onClick() {
        btn_search.setOnClickListener {
            SearchActivity.launchIntent(context)
        }

        btn_like.setOnClickListener {

        }
    }

    private fun initRecyclerCategory() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycler_category.layoutManager = layoutManager
        adapterCategory.emptyText = resources.getString(R.string.empty)
        recycler_category.adapter = adapterCategory

        adapterCategory.setOnRetryListener {
            viewModel.getData()
        }
    }

    private fun initRecyclerProduct() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_product.isNestedScrollingEnabled = false
        recycler_product.layoutManager = layoutManager
        adapterProduct.emptyText = resources.getString(R.string.empty)
        recycler_product.adapter = adapterProduct

        adapterProduct.setOnRetryListener {
            viewModel.getData()
        }

        adapterProduct.onProductClick = { dataProduct ->
            ProductDetailActivity.launchIntent(context = context, data = dataProduct)
        }
    }

    fun pageState() {
        (activity as MainActivity).navSelected(Variables.bottomNavigation.HOME)
    }
}