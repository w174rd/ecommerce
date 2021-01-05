package com.najib.ecommerce.api.util

import com.najib.ecommerce.App.Companion.context
import com.najib.ecommerce.BuildConfig
import com.najib.ecommerce.R
import com.najib.ecommerce.model.CoreResponse
import com.najib.ecommerce.model.ErrorResponse
import com.najib.ecommerce.model.home.DataHome
import com.najib.ecommerce.util.Functions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

abstract class CustomObserver<RESPONSE : ArrayList<CoreResponse<DataHome>>> : Observer<RESPONSE> {

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(response: RESPONSE) {
        try {
            onSuccess(response)
        } catch (e: Exception) {
            val code = 0
            var message = ""
            if (BuildConfig.DEBUG) {
                message = e.message.toString()
                e.printStackTrace().toString() + ""
            } else {
                message = context.getString(R.string.error_unknown)
            }
            onFailure(ErrorResponse(error = 1, code = code, message = message))
            Functions.printStackTrace(e)
        }
    }

    override fun onError(throwable: Throwable) {
        var code = 0
        var message = ""

        if (throwable is HttpException) {
            message = if (BuildConfig.DEBUG) throwable.message().toString() + ""
            else context.getString(R.string.error_unknown)
            code = throwable.code()
        } else if (throwable is UnknownHostException || throwable is SSLException || throwable is ConnectException) {
            message = context.getString(R.string.error_conn_generic)
        } else if (throwable is SocketTimeoutException) {
            message = context.getString(R.string.error_conn_time_out)
        } else {
            message = if (BuildConfig.DEBUG) throwable.printStackTrace().toString() + ""
            else context.getString(R.string.error_unknown)
        }

        onFailure(ErrorResponse(1, code, message))
    }

    abstract fun onSuccess(response: RESPONSE)

    abstract fun onFailure(error: Any)
}