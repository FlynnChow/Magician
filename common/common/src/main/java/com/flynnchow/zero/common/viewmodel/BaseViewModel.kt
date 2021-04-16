package com.flynnchow.zero.common.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flynnchow.zero.common.proxy_api.CoroutineProxy
import com.flynnchow.zero.common.proxy_impl.CoroutineProxyImpl

abstract class BaseViewModel : ViewModel(), CoroutineProxy by CoroutineProxyImpl() {

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast //显示toast

    private val _finish = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean> = _finish //activity.finish()

    private val _backPress = MutableLiveData<Boolean>()
    val backPress: LiveData<Boolean> = _backPress//activity.onBackPress()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        clearCoroutine()
    }

    fun showToast(toast:String){
        _toast.value = toast
    }

    fun finish(){
        _finish.value = true
    }
}