package com.najib.ecommerce.view.activity.core

import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.najib.ecommerce.R

open class CoreActivity : AppCompatActivity() {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    val GOOGLE_RC_SIGN_IN = 1000

    fun buildToolbar(toolbar: Toolbar): Boolean {
        var statusToolbar = false
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.elevation = 0f
            statusToolbar = true
        }

        return statusToolbar
    }

    fun toolbarBack(toolbar: Toolbar, title: TextView? = null) {
        if (buildToolbar(toolbar)) {
            title?.text = this.title
            toolbar.setNavigationIcon(android.R.drawable.arrow_down_float)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    /** ============== FACEBOOK =============== */

    fun signInFacebook() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
    }

    fun signOutFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }

        GraphRequest(AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            GraphRequest.Callback { LoginManager.getInstance().logOut() }).executeAsync()
    }

    /** ================ GOOGLE ================= */

    fun initialGoogleAccount() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile()
            .requestIdToken(resources.getString(R.string.google_web_client_id)).requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Log.d("GO-TKN", account.idToken.toString())
        }
    }

    fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN)
    }

    fun signOutGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener {}
    }
}