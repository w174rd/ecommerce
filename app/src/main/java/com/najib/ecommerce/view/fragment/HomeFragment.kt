package com.najib.ecommerce.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.najib.ecommerce.R
import com.najib.ecommerce.api.util.OnResponse
import com.najib.ecommerce.util.Functions
import com.najib.ecommerce.util.Variables
import com.najib.ecommerce.view.activity.MainActivity
import com.najib.ecommerce.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.getData()
        viewModel.onResponse.observe(this, Observer {
            when (it.status) {
                OnResponse.LOADING -> {
                    Functions.toast(context, "loding...")
                }
                OnResponse.SUCCESS -> {

                }
                OnResponse.ERROR -> {
                    Functions.toast(context, it.error.toString())
                }
            }
        })
        viewModel.liveData.observe(this, Observer { data ->
            var result: String? = null
            data.category?.forEach {
                result = it.name
            }
            Functions.toast(context, result.toString())
        })
    }

    fun pageState() {
        (activity as MainActivity).navSelected(Variables.bottomNavigation.HOME)
    }
}