package com.flynnchow.zero.magician.func.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.flynnchow.zero.base.util.StringUtils
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityFuncBaseBinding
import com.flynnchow.zero.magician.func.view.BaiduPanFragment
import com.flynnchow.zero.magician.func.view.VideoFragment
import com.flynnchow.zero.magician.func.view.VideoPhotoFragment
import com.flynnchow.zero.magician.func.viewmodel.FuncViewModel

class FuncBaseActivity : MagicianActivity<ActivityFuncBaseBinding>(R.layout.activity_func_base) {
    companion object {
        fun launchVideoPage(context: Context) = launchFuncPage(context, "video")

        fun launchVideoPhotoPage(context: Context) = launchFuncPage(context, "video_photo")

        fun launchBaiduPanPage(context: Context) = launchFuncPage(context, "baidu_pan")

        private fun launchFuncPage(context: Context, tag: String) {
            context.startActivity(Intent(context, FuncBaseActivity::class.java).apply {
                putExtra("container_type", tag)
            })
        }
    }

    private val mViewModel by lazy {
        getViewModel(FuncViewModel::class.java)
    }

    override fun onInitView() {
        initContainer()
        mBinding.back.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }

    private fun initContainer() {
        val containerType = intent.getStringExtra("container_type")
        if (StringUtils.isNotEmpty(containerType)) {
            when (containerType) {
                "video" -> setContainer(VideoFragment())
                "video_photo" -> setContainer(VideoPhotoFragment())
                "baidu_pan" -> setContainer(BaiduPanFragment())
            }
        }
    }

    private fun setContainer(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(mBinding.container.id, fragment)
            .commitAllowingStateLoss()
    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = mViewModel
    }
}