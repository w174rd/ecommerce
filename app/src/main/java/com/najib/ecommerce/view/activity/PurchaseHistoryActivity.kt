package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.ProductPromo
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.GlobalHawk
import com.najib.ecommerce.view.activity.core.CoreActivity
import com.najib.ecommerce.view.adapter.ProductMinAdapter
import kotlinx.android.synthetic.main.activity_purchase_history.*
import kotlinx.android.synthetic.main.toolbar.*

class PurchaseHistoryActivity : CoreActivity() {

    private val adapterProduct = ProductMinAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)
        initView()
    }

    private fun initView() {
        toolbarBack(
            toolbarView = toolbar_top,
            toolbarText = toolbar_title,
            title = resources.getString(R.string.purchase_history)
        )
        initRecyclerProduct()
        setDataPurchase()
    }

    private fun setDataPurchase() {
        try {
            val puchaseDataHawk: MutableMap<String, ProductPromo> = GlobalHawk.getPurchaseHistory()
            val puchaseData = ArrayList<ProductPromo>()
            puchaseDataHawk.forEach { (key, value) ->
                puchaseData.add(value)
            }
            adapterProduct.clearDataList()
            adapterProduct.setDataList(puchaseData)
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    private fun initRecyclerProduct() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_product.isNestedScrollingEnabled = false
        recycler_product.layoutManager = layoutManager
        adapterProduct.emptyText = resources.getString(R.string.empty)
        recycler_product.adapter = adapterProduct

        adapterProduct.setOnRetryListener {

        }

        adapterProduct.onProductClick = { dataProduct ->
            ProductDetailActivity.launchIntent(context = this, data = dataProduct)
        }

        adapterProduct.onProductRemove = { dataProduct, position ->
            try {
                try {
                    val puchaseData: MutableMap<String, ProductPromo> =
                        GlobalHawk.getPurchaseHistory()
                    puchaseData.remove(key = dataProduct?.id)
                    GlobalHawk.setPurchaseHistory(puchaseData)
                } catch (e: Exception) {
                    Functions.printStackTrace(e)
                } finally {
                    setDataPurchase()
                    adapterProduct.notifyItemRemoved(position)
                    Functions.toast(
                        this,
                        resources.getString(R.string.product_has_been_removed)
                    )
                }
            } catch (e: Exception) {
                Functions.printStackTrace(e)
            }
        }
    }

    companion object {
        fun launchIntent(context: Context?) {
            context?.let {
                val intent = Intent(it, PurchaseHistoryActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}