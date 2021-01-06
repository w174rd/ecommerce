package com.najib.ecommerce.view.activity.core

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.najib.ecommerce.R

open class CoreActivity : AppCompatActivity() {

    private fun buildToolbar(toolbar: Toolbar): Boolean {
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
}