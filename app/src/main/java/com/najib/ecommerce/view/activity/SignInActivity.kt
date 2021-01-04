package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.najib.ecommerce.R
import com.najib.ecommerce.util.ValidationHelper
import com.najib.ecommerce.view.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : CoreActivity() {

    private val callbackManager = CallbackManager.Factory.create()
    private val GOOGLE_RC_SIGN_IN = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initView()
    }

    private fun initView() {
        signOutFacebook()
        initialFacebook()
        onClick()
    }

    private fun onClick() {
        btn_login.setOnClickListener {
            if (isDataValid()) {
                finish()
                MainActivity.launchIntent(this@SignInActivity)
            }
        }

        btn_login_facebook.setOnClickListener {
            signInFacebook()
        }

        btn_login_google.setOnClickListener {

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

    /** ============ FACEBOOK ============= */
    private fun initialFacebook() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val accessToken = loginResult.accessToken
                    MainActivity.launchIntent(this@SignInActivity)
                    finish()
                }

                override fun onCancel() {

                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(
                        this@SignInActivity,
                        exception.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    /** ================ Activity Result ============== */

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            /** GOOGLE */
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            if (task.isSuccessful) handleSignInResult(task)
        } else {
            /** FACEBOOK */
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
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