package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.najib.ecommerce.R
import com.najib.ecommerce.view.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initView()
    }

    private fun initView() {
        onClick()
    }

    private fun onClick() {
        btn_login.setOnClickListener {

        }

        btn_login_facebook.setOnClickListener {

        }

        btn_login_google.setOnClickListener {

        }
    }

    companion object {

        fun launchIntent(context: Context?) {
            context?.let {
                val intent = Intent(it, SignInActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}