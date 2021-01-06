package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.najib.ecommerce.R
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.ValidationHelper
import com.najib.ecommerce.view.activity.core.CoreActivity
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : CoreActivity() {

    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialGoogleAccount()
        initialFacebook()
        setContentView(R.layout.activity_signin)

        initView()
    }

    private fun initView() {
        signOutGoogle()
        signOutFacebook()
        onClick()
    }

    private fun onClick() {
        btn_login.setOnClickListener {
            if (isDataValid()) {
                openDashboard()
            }
        }

        btn_login_facebook.setOnClickListener {
            signInFacebook()
        }

        btn_login_google.setOnClickListener {
            signInGoogle()
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
        try {
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
//                    val accessToken = loginResult.accessToken
                        openDashboard()
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
        }catch (e: Exception){
            Functions.printStackTrace(e)
        }
    }

    /** ============== GOOGLE =============== */

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                val idToken = it.idToken
                val email = it.email
                if (idToken != null && email != null) {
                    openDashboard()
                }
                Log.d("GO-TKN", idToken.toString())
            }
        } catch (e: ApiException) {
            Toast.makeText(this@SignInActivity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    /** ================ Activity Result ============== */

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            /** GOOGLE */
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) handleSignInResult(task)
        } else {
            /** FACEBOOK */
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDashboard() {
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