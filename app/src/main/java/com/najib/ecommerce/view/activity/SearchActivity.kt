package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.najib.ecommerce.R
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.GlobalHawk
import com.najib.ecommerce.view.activity.core.CoreActivity
import com.najib.ecommerce.view.adapter.ProductMinAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : CoreActivity() {

    private val adapterProduct = ProductMinAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initRecyclerProduct()
        initView()
        onClick()
    }

    private fun initView() {

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    adapterProduct.clearDataList()
                } else {
                    try {
                        adapterProduct.clearDataList()
                        val dataProduct = GlobalHawk.getProduct()
                        adapterProduct.setDataList(dataProduct)
                    } catch (e: Exception) {
                        Functions.printStackTrace(e)
                    }
                }
            }
        })
    }

    private fun onClick() {
        btn_back.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerProduct() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_product.isNestedScrollingEnabled = false
        recycler_product.layoutManager = layoutManager
        adapterProduct.emptyText = ""
        recycler_product.adapter = adapterProduct

        adapterProduct.setOnRetryListener {

        }

        adapterProduct.onProductClick = { dataProduct ->
            ProductDetailActivity.launchIntent(context = this, data = dataProduct)
        }
    }

    companion object {
        fun launchIntent(context: Context?) {
            context?.let {
                val intent = Intent(it, SearchActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}