package com.flynnchow.zero.common.activity

import androidx.annotation.RawRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.flynnchow.zero.common.viewmodel.BaseViewModel

abstract class BindingActivity<DataBinding : ViewDataBinding>(@RawRes private val resId: Int?) :
    BaseActivity(resId) {
    protected lateinit var mBinding: DataBinding

    final override fun createView(resId: Int) {
        mBinding = DataBindingUtil.setContentView(this,resId)
        mBinding.lifecycleOwner = this
    }
}