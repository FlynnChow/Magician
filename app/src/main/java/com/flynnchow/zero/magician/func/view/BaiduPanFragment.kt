package com.flynnchow.zero.magician.func.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.helper.BaiduPanHelper
import com.flynnchow.zero.magician.func.adapter.BaiduPanAdapter
import com.flynnchow.zero.magician.func.base.FuncFragment

class BaiduPanFragment: FuncFragment() {
    private val adapter = BaiduPanAdapter()

    override fun onInitView() {
        funcViewModel.setTitle("网盘")

        mBinding.listView.layoutManager = GridLayoutManager(requireContext(),3)
        mBinding.listView.adapter = adapter
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {
        startLaunch({
            mBinding.loadingView.visibility = View.VISIBLE
            mBinding.loadingHint = "正在从网盘获取数据,请稍等…"
            val list = BaiduPanHelper.getBaiduPanImages()
            list?.let {
                adapter.setData(it)
                funcViewModel.setHint("已备份${adapter.itemCount}个照片")
            }
        },{
          showEmptyView("加载失败，请稍后重试")
        },{
            mBinding.loadingView.visibility = View.GONE
        })
    }

}