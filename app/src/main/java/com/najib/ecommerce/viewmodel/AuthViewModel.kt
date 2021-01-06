package com.najib.ecommerce.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.najib.ecommerce.App
import com.najib.ecommerce.R
import com.najib.ecommerce.api.util.OnResponse
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.Variables.GOOGLE_RC_SIGN_IN

class AuthViewModel : ViewModel() {

    val onResponse = MutableLiveData<OnResponse<Any>>()

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var callbackManager: CallbackManager? = null

    /** ============ FACEBOOK ============= */

    fun initialFacebook(mCallbackManager: CallbackManager) {
        try {
            callbackManager = mCallbackManager

            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        onResponse.postValue(OnResponse.success())
                    }

                    override fun onCancel() {

                    }

                    override fun onError(exception: FacebookException) {
                        onResponse.postValue(OnResponse.error(exception))
                    }
                })
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    fun signInFacebook(context: Activity) {
        try {
            LoginManager.getInstance()
                .logInWithReadPermissions(context, mutableListOf("email", "public_profile"))
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    fun signOutFacebook() {
        try {
            if (AccessToken.getCurrentAccessToken() == null) {
                return  // already logged out
            }

            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE
            ) { LoginManager.getInstance().logOut() }.executeAsync()
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    /** ================ GOOGLE ================= */

    fun initialGoogleAccount(activity: Activity) {
        try {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile()
                    .requestIdToken(App.context.resources.getString(R.string.google_web_client_id))
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

            val account = GoogleSignIn.getLastSignedInAccount(activity)
            if (account != null) {
                Log.d("GO-TKN", account.idToken.toString())
            }
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    fun signInGoogle(context: Activity) {
        try {
            val signInIntent = mGoogleSignInClient?.signInIntent
            context.startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN)
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    fun signOutGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener {}
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                val idToken = it.idToken
                val email = it.email
                if (idToken != null && email != null) {
                    onResponse.postValue(OnResponse.success())
                }
                Log.d("GO-TKN", idToken.toString())
            }
        } catch (e: ApiException) {
            onResponse.postValue(OnResponse.error(e))
        }
    }

    /** ================ Activity Result ============== */

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            /** GOOGLE */
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) handleSignInResult(task)
        } else {
            /** FACEBOOK */
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }
}