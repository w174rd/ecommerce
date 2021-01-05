package com.najib.ecommerce.api

import com.najib.ecommerce.api.util.OkHttpClientHelper
import com.najib.ecommerce.model.CoreResponse
import com.najib.ecommerce.model.home.DataHome
import com.najib.ecommerce.util.Variables
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object APIService {

    val apiInterface: APIInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Variables.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClientHelper().initOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(APIInterface::class.java)
    }

    interface APIInterface {
        @GET("home")
        fun getHomeData(): Observable<ArrayList<CoreResponse<DataHome>>>
    }
}