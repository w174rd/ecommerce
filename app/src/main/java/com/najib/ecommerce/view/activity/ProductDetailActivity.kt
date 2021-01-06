package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.ProductPromo
import com.najib.ecommerce.util.Variables
import com.najib.ecommerce.view.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class ProductDetailActivity : CoreActivity() {

    private var data: ProductPromo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        initView()
    }

    private fun initView() {
        data = intent.getParcelableExtra(Variables.key.PUT_EXTRA_KEY)

        toolbarBack(
            toolbarView = toolbar_top,
            toolbarText = toolbar_title,
            title = resources.getString(R.string.product_detail)
        )

        data?.let {
            img_product.setImageURI(it.imageUrl)
            txt_title.text = it.title
            if (it.loved == 1) {
                img_like.setImageResource(R.drawable.ic_like_full)
            } else {
                img_like.setImageResource(R.drawable.ic_like)
            }
            txt_description.text = it.description
            txt_price.text = it.price
        }
    }

    companion object {
        fun launchIntent(context: Context?, data: ProductPromo?) {
            context?.let {
                val intent = Intent(it, ProductDetailActivity::class.java)
                intent.putExtra(Variables.key.PUT_EXTRA_KEY, data)
                it.startActivity(intent)
            }
        }
    }
}