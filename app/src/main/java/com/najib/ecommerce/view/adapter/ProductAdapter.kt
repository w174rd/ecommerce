package com.najib.ecommerce.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.ProductPromo
import com.najib.ecommerce.util.OnClickProduct
import kotlinx.android.synthetic.main.adapter_product.view.*

class ProductAdapter() :
    CoreRecyclerViewAdapter<ProductPromo, ProductAdapter.ViewHolder>() {

    var onProductClick: OnClickProduct? = null

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_product))
    }

    override fun onBindDataViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
            val data = getDataList()[adapterPosition]

            itemView.apply {
                img_product.setImageURI(data.imageUrl)
                txt_product.text = data.title
                if (data.loved == 1) {
                    img_like.setImageResource(R.drawable.ic_like_full)
                } else {
                    img_like.setImageResource(R.drawable.ic_like)
                }

                setOnClickListener {
                    onProductClick?.invoke(data)
                }
            }
        }
    }
}