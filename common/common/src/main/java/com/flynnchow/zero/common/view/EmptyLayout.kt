package com.flynnchow.zero.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.flynnchow.zero.common.R
import com.flynnchow.zero.common.databinding.PanelEmptyBinding
import com.flynnchow.zero.common.framework_api.EmptyView

class EmptyLayout @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, style: Int = 0) :
    FrameLayout(context, attr, style), EmptyView {
    private var emptyBinding: PanelEmptyBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.panel_empty, this, false)
    init {
        addView(emptyBinding.rootView)
        emptyBinding.rootView.visibility = View.GONE
    }

    override fun hideEmptyView() {
        for (child in children) {
            child.visibility = View.VISIBLE
        }
        emptyBinding.rootView.visibility = View.GONE
    }

    override fun showEmptyView(hint: String) {
        for (child in children) {
            child.visibility = View.GONE
        }
        emptyBinding.rootView.visibility = View.VISIBLE
        emptyBinding.content = hint
    }
}