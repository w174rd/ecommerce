package com.najib.ecommerce.model

data class ErrorResponse(
    var error: Int? = null,
    var code: Int? = null,
    var message: String? = null
)