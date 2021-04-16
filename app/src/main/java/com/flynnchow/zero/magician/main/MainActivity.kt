package com.flynnchow.zero.magician.main

import android.Manifest
import android.os.Bundle
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ActivityMainBinding


class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val pageAdapter by lazy { MainPageAdapter(supportFragmentManager, mBinding) }

    override fun onInitView() {
        pageAdapter.switchPage(mBinding.mainNav.mainGallery.mainTab)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ), null,
            { result, _ ->
                if (!result) {
                    showToast("需要开启文件读写权限才能使用相册")
                    finish()
                } else {
                    initMedia()
                }
            })
    }

    private fun initMedia() {
        MediaProvider.instance.doWork()
    }
}