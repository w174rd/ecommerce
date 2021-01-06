package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.ProductPromo
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.GlobalHawk
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
        onClick()
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

    private fun onClick() {
        btn_buy.setOnClickListener {
            try {
                val puchaseData: MutableMap<String, ProductPromo> = GlobalHawk.getPurchaseHistory()
                data?.let {
                    puchaseData[it.id ?: "0"] = it
                    GlobalHawk.setPurchaseHistory(puchaseData)
                }
            } catch (e: Exception) {
                Functions.printStackTrace(e)
            } finally {
                Functions.toast(
                    this,
                    resources.getString(R.string.product_has_been_added_to_the_basket)
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                Functions.share(context = this, text = data?.description)
                true
            }
            else -> super.onOptionsItemSelected(item)
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