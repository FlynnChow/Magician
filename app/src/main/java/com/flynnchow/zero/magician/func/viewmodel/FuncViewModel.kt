package com.flynnchow.zero.magician.func.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel

class FuncViewModel:BaseViewModel() {
    private val _title = MutableLiveData<String>()
    val title:LiveData<String> = _title

    private val _hint = MutableLiveData<String>()
    val hint:LiveData<String> = _hint

    fun setTitle(title: String){
        _title.value = title
    }

    fun setHint(hint:String){
        _hint.value = hint
    }
}