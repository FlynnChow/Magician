package com.flynnchow.zero.common.activity

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.flynnchow.zero.common.R
import com.flynnchow.zero.common.databinding.BaseRootViewBinding
import com.flynnchow.zero.common.framework_api.EmptyView

abstract class MagicianActivity<DataBinding : ViewDataBinding>(@LayoutRes private val resId: Int?) :
    BindingActivity<DataBinding>(resId), EmptyView {
    private lateinit var mDecoderBinding: BaseRootViewBinding

    override fun setRootView(resId: Int) {
        mDecoderBinding = DataBindingUtil.setContentView(this, R.layout.base_root_view)
        mBinding = DataBindingUtil.inflate(layoutInflater, resId, mDecoderBinding.decoderView, false)
        mDecoderBinding.decoderView.addView(mBinding.root,0)
        mBinding.lifecycleOwner = this
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