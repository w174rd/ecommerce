package com.najib.ecommerce.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.najib.ecommerce.api.APIService
import com.najib.ecommerce.api.util.CustomObserver
import com.najib.ecommerce.api.util.OnResponse
import com.najib.ecommerce.model.CoreResponse
import com.najib.ecommerce.model.home.DataHome
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel(), LifecycleObserver {
    val liveData = MutableLiveData<DataHome>()
    val onResponse = MutableLiveData<OnResponse<Any>>()

    private var apiService = APIService.apiInterface

    fun getData() {
        onResponse.postValue(OnResponse.loading())
        apiService.getHomeData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CustomObserver<ArrayList<CoreResponse<DataHome>>>() {

                override fun onSuccess(response: ArrayList<CoreResponse<DataHome>>) {
                    var coreResponse: CoreResponse<DataHome>? = null
                    response.forEach {
                        coreResponse = it
                    }
                    onResponse.postValue(OnResponse.success())
                    liveData.postValue(coreResponse?.data)
                }

                override fun onFailure(error: Any) {
                    onResponse.postValue(OnResponse.error(error))
                }
            })
    }
}