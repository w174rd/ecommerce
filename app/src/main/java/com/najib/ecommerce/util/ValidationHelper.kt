package com.najib.ecommerce.util

import android.view.View
import android.widget.TextView

object ValidationHelper {

    fun isEmpty(textView: TextView?): Boolean {
        return if (textView?.visibility == View.VISIBLE) {
            isEmpty(textView.text.toString())
        } else {
            false
        }
    }

    fun isEmpty(message: String?): Boolean {
        return if (message == null) {
            true
        } else {
            if (message.isEmpty()) {
                true
            } else {
                message.replace(" ".toRegex(), "").isEmpty()
            }
        }
    }
}