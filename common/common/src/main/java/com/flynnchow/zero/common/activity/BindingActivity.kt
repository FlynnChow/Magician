package com.flynnchow.zero.common.activity

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RawRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.flynnchow.zero.common.databinding.BaseRootViewBinding
import com.flynnchow.zero.common.viewmodel.BaseViewModel

abstract class BindingActivity<DataBinding : ViewDataBinding>(@LayoutRes private val resId: Int?) :
    BaseActivity(resId) {
    protected lateinit var mBinding: DataBinding

    override fun setRootView(resId: Int) {
        mBinding = DataBindingUtil.setContentView(this,resId)
        mBinding.lifecycleOwner = this
    }
}