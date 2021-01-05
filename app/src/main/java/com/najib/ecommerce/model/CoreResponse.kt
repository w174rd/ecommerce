package com.najib.ecommerce.model

import com.google.gson.annotations.SerializedName

data class CoreResponse<RESPONSE>(
    @SerializedName("data")
    var data: RESPONSE? = null
)