package com.flynnchow.zero.magician.main

import android.os.Bundle
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityMainBinding


class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val pageAdapter by lazy { MainPageAdapter(supportFragmentManager, mBinding) }

    override fun onInitView() {
        pageAdapter.switchPage(mBinding.mainGallery)
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }
}