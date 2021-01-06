package com.najib.ecommerce.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.ProductPromo
import com.najib.ecommerce.util.OnClickProduct
import com.najib.ecommerce.util.OnRemoveProduct
import kotlinx.android.synthetic.main.adapter_product.view.img_product
import kotlinx.android.synthetic.main.adapter_product.view.txt_product
import kotlinx.android.synthetic.main.adapter_product_min.view.*

class ProductMinAdapter : CoreRecyclerViewAdapter<ProductPromo, ProductMinAdapter.ViewHolder>() {

    var onProductClick: OnClickProduct? = null
    var onProductRemove: OnRemoveProduct? = null

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_product_min))
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
                txt_price.text = data.price
                setOnClickListener {
                    onProductClick?.invoke(data)
                }
                setOnLongClickListener {
                    onProductRemove?.invoke(data, adapterPosition)
                    true
                }
            }
        }
    }
}