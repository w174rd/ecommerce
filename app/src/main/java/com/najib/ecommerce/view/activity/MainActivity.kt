package com.najib.ecommerce.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.najib.ecommerce.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {

        fun launchIntent(context: Context?) {
            context?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}