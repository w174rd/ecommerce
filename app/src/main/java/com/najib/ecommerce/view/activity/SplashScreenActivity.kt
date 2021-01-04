package com.najib.ecommerce.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import com.najib.ecommerce.R
import com.najib.ecommerce.view.activity.core.CoreActivity

class SplashScreenActivity : CoreActivity() {

    private var mSplashScreenRunnable: Runnable? = null
    private var mSplashScreenHandler: Handler? = null
    private var isSplashScreenLoading = false
    private var isApplicationReady = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spashscreen)

        waitTime()
        loadSplashScreen()
        isSplashScreenLoading = true
    }

    override fun onPause() {
        super.onPause()
        isSplashScreenLoading = false
        mSplashScreenHandler?.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        if (isApplicationReady) {
            loadSplashScreen()
            isSplashScreenLoading = true
        }
    }

    private fun waitTime() {
        isApplicationReady = true
        if (mSplashScreenRunnable == null) {
            mSplashScreenRunnable = Runnable {
                if (!isFinishing) {
//                    MainActivity.launchIntent(this@SplashScreenActivity)
                    finish()
                }
            }
        }

        if (mSplashScreenHandler == null) mSplashScreenHandler = Handler()
    }

    private fun loadSplashScreen() {
        if (!isSplashScreenLoading) {
            mSplashScreenRunnable?.let {
                mSplashScreenHandler?.postDelayed(it, 2000)
            }
        }
    }
}