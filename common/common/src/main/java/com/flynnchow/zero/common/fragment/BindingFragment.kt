package com.flynnchow.zero.common.fragment

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingFragment<DataBinding : ViewDataBinding>(@LayoutRes private val resId: Int?) :
    BaseFragment(resId) {
    protected lateinit var mBinding: DataBinding

    final override fun createView(resId: Int, container: ViewGroup?): View {
        mBinding = DataBindingUtil.inflate(layoutInflater, resId, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }
}