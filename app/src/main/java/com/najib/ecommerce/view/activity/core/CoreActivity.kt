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
import com.najib.ecommerce.util.Functions

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

    fun toolbarBack(toolbarView: Toolbar, toolbarText: TextView? = null, title: String? = null) {
        if (buildToolbar(toolbarView)) {
            if (title != null) toolbarText?.text = title
            else toolbarText?.text = this.title

            toolbarView.setNavigationIcon(R.drawable.ic_left_arrow)
            toolbarView.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    /** ============== FACEBOOK =============== */

    fun signInFacebook() {
        try {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
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

    fun initialGoogleAccount() {
        try {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile()
                    .requestIdToken(resources.getString(R.string.google_web_client_id))
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                Log.d("GO-TKN", account.idToken.toString())
            }
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    fun signInGoogle() {
        try {
            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN)
        } catch (e: Exception) {
            Functions.printStackTrace(e)
        }
    }

    fun signOutGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener {}
    }

    fun clearAllSosmed() {
        signOutGoogle()
        signOutFacebook()
    }
}