package com.flynnchow.zero.common.fragment

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.flynnchow.zero.common.R
import com.flynnchow.zero.common.databinding.BaseRootViewBinding
import com.flynnchow.zero.common.framework_api.EmptyView

abstract class MagicianFragment<DataBinding : ViewDataBinding>(@LayoutRes private val resId: Int?) :
    BindingFragment<DataBinding>(resId), EmptyView {
    private lateinit var mDecoderBinding: BaseRootViewBinding

    override fun createView(resId: Int, container: ViewGroup?): View {
        mDecoderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.base_root_view, container, false)
        mBinding = DataBindingUtil.inflate(layoutInflater, resId, mDecoderBinding.decoderView, false)
        mDecoderBinding.decoderView.addView(mBinding.root,0)
        mBinding.lifecycleOwner = this
        return mDecoderBinding.root
    }

    override fun hideEmptyView() {
        mBinding.root.visibility = View.VISIBLE
        mDecoderBinding.emptyView.root.visibility = View.GONE
    }

    override fun showEmptyView(hint: String) {
        mBinding.root.visibility = View.GONE
        mDecoderBinding.emptyView.root.visibility = View.VISIBLE
        mDecoderBinding.emptyView.content = hint
    }
}