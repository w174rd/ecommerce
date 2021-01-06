package com.najib.ecommerce

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.orhanobut.hawk.Hawk

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = this

        Hawk.init(context).build()
        Fresco.initialize(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}