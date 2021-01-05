package com.najib.ecommerce.util

import android.content.Context
import android.widget.Toast
import com.najib.ecommerce.BuildConfig

object Functions {
    fun printStackTrace(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
    }

    fun toast(context: Context?, text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}