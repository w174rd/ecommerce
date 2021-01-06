package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.facebook.*
import com.najib.ecommerce.R
import com.najib.ecommerce.api.util.OnResponse
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.ValidationHelper
import com.najib.ecommerce.view.activity.core.CoreActivity
import com.najib.ecommerce.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : CoreActivity() {

    private var callbackManager = CallbackManager.Factory.create()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initialViewModel()
        initView()
    }

    private fun initialViewModel() {
        viewModel.onResponse.observe(this, {
            when (it.status) {
                OnResponse.LOADING -> {
                    Functions.toast(this, "Loading...")
                }
                OnResponse.SUCCESS -> {
                    openDashboard()
                }
                OnResponse.ERROR -> {
                    Functions.toast(this, it.error.toString())
                }
            }
        })
    }

    private fun initView() {
        viewModel.initialFacebook(callbackManager)
        viewModel.initialGoogleAccount(this)
        onClick()
    }

    private fun onClick() {
        btn_login.setOnClickListener {
            if (isDataValid()) {
                openDashboard()
            }
        }

        btn_login_facebook.setOnClickListener {
            viewModel.signInFacebook(this)
        }

        btn_login_google.setOnClickListener {
            viewModel.signInGoogle(this)
        }
    }

    private fun isDataValid(): Boolean {
        var isValid = true
        clearField()

        if (et_username.text != null && ValidationHelper.isEmpty(et_username.text.toString())) {
            layout_username.error = resources.getString(R.string.error_this_field_required)
            isValid = false
        }

        if (et_password.text != null && ValidationHelper.isEmpty(et_password.text.toString())) {
            layout_password.error = resources.getString(R.string.error_this_field_required)
            isValid = false
        }

        return isValid
    }

    private fun clearField() {
        layout_username.isErrorEnabled = false
        layout_password.isErrorEnabled = false
    }

    /** ================ Activity Result ============== */

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(
            requestCode = requestCode,
            resultCode = resultCode,
            data = data
        )
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDashboard() {
        viewModel.signOutFacebook()
        viewModel.signOutGoogle()
        MainActivity.launchIntent(this@SignInActivity)
        finish()
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