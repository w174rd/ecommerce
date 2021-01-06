package com.najib.ecommerce.util

import com.najib.ecommerce.R
import com.najib.ecommerce.model.home.ProductPromo

object Variables {

    val apiUrl = "https://private-4639ce-ecommerce56.apiary-mock.com/"

    object key {
        val PUT_EXTRA_KEY = "PUT_EXTRA_KEY"
        val HAWK_PURCHASE_HISTORY = "HAWK_PURCHASE_HISTORY"
        val HAWK_PRODUCT = "HAWK_PRODUCT"
    }

    object bottomNavigation {
        internal var HOME = R.id.menu_home
        internal var FEED = R.id.menu_feed
        internal var CART = R.id.menu_cart
        internal var PROFILE = R.id.menu_profile
    }
}

typealias OnClickProduct = ((data: ProductPromo?) -> Unit)
typealias OnRemoveProduct = ((data: ProductPromo?, position: Int) -> Unit)