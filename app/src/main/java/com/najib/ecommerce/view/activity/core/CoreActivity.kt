package com.najib.ecommerce.view.activity.core

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager

open class CoreActivity : AppCompatActivity() {

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
        LoginManager.getInstance().logInWithReadPermissions(this, mutableListOf("email", "public_profile"))
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
}