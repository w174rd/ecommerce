package com.najib.ecommerce.api.util

class OnResponse<T> private constructor(
    var status: Int,
    var data: T? = null,
    var error: T? = null
) {

    companion object {

        const val LOADING = 0
        const val SUCCESS = 1
        const val ERROR = 2

        fun <T> loading(data: T? = null): OnResponse<T> {
            return OnResponse(status = LOADING, data = data)
        }

        fun <T> success(data: T? = null): OnResponse<T> {
            return OnResponse(status = SUCCESS, data = data)
        }

        fun <T> error(error: T, data: T? = null): OnResponse<T> {
            return OnResponse(status = ERROR, data = data, error = error)
        }
    }
}