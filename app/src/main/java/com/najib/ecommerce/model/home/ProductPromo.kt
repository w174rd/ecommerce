package com.najib.ecommerce.model.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductPromo(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: String? = null,
    val imageUrl: String? = null,
    var loved: Int? = null
) : Parcelable