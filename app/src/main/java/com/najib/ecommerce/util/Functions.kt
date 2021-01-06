package com.najib.ecommerce.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.najib.ecommerce.BuildConfig


object Functions {
    fun printStackTrace(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
    }

    fun toast(context: Context?, text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun share(context: Context?, text: String?){
        val myIntent = Intent(Intent.ACTION_SEND)
        myIntent.type = "text/plain"
        myIntent.putExtra(Intent.EXTRA_SUBJECT, text)
        myIntent.putExtra(Intent.EXTRA_TEXT, text)
        context?.startActivity(Intent.createChooser(myIntent, "Share using"))
    }
}