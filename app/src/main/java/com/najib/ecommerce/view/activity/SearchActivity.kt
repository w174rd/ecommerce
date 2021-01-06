package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.najib.ecommerce.R
import com.najib.ecommerce.view.activity.core.CoreActivity

class SearchActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
    }

    private fun initView(){

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