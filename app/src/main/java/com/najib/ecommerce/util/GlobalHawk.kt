package com.najib.ecommerce.util

import com.najib.ecommerce.model.home.ProductPromo
import com.orhanobut.hawk.Hawk

object GlobalHawk {

    fun getPurchaseHistory(): MutableMap<String, ProductPromo> {
        return Hawk.get(Variables.key.HAWK_PURCHASE_HISTORY, mutableMapOf())
    }

    fun setPurchaseHistory(data: Any) {
        Hawk.delete(Variables.key.HAWK_PURCHASE_HISTORY)
        Hawk.put(Variables.key.HAWK_PURCHASE_HISTORY, data)
    }

    fun deletePurchaseHistory() {
        Hawk.delete(Variables.key.HAWK_PURCHASE_HISTORY)
    }

    fun getProduct(): ArrayList<ProductPromo>? {
        return Hawk.get(Variables.key.HAWK_PRODUCT)
    }

    fun setProduct(data: ArrayList<ProductPromo>?) {
        Hawk.delete(Variables.key.HAWK_PRODUCT)
        Hawk.put(Variables.key.HAWK_PRODUCT, data)
    }
}