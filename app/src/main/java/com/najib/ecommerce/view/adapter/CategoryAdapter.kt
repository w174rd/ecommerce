package com.najib.ecommerce.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.Category
import kotlinx.android.synthetic.main.adapter_category.view.*

class CategoryAdapter : CoreRecyclerViewAdapter<Category, CategoryAdapter.ViewHolder>() {
    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_category))
    }

    override fun onBindDataViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            val data = getDataList()[adapterPosition]

            itemView.apply {
                img_category.setImageURI(data.imageUrl)
                txt_category.text = data.name
            }
        }
    }
}