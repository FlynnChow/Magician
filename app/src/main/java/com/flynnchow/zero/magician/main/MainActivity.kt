package com.flynnchow.zero.magician.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ActivityMainBinding
import com.flynnchow.zero.magician.debug.DebugActivity
import com.flynnchow.zero.magician.func.base.FuncBaseActivity
import com.flynnchow.zero.magician.setting.SettingFragment
import kotlinx.coroutines.delay


class MainActivity : MagicianActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val pageAdapter by lazy { MainPageAdapter(supportFragmentManager, mBinding) }
    private val settingFragment: SettingFragment by lazy {
        SettingFragment()
    }

    override fun onInitView() {
        pageAdapter.switchPage(mBinding.mainNav.mainGallery.mainTab)
        initDrawerFragment()
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

    private fun initDrawerFragment() {
        supportFragmentManager.beginTransaction()
            .replace(mBinding.drawContainer.id, settingFragment, settingFragment.javaClass.name)
            .commitAllowingStateLoss()
        mBinding.mainNav.mainConfig.setOnClickListener {
            mBinding.drawer.openDrawer(GravityCompat.START)
        }
        mBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onBackPressed() {
        if (mBinding.drawer.isDrawerOpen(GravityCompat.START)){
            mBinding.drawer.closeDrawer(GravityCompat.START)
        }else
            super.onBackPressed()
    }

    fun onClickFunc(name:String?){
        name?.let {
            when(it){
                "视频"->{
                    FuncBaseActivity.launchVideoPage(this)
                }
                "影集"->{
                    FuncBaseActivity.launchVideoPhotoPage(this)
                }
                "网盘"->{
                    FuncBaseActivity.launchBaiduPanPage(this)
                }
            }
        }
        mBinding.drawer.closeDrawer(GravityCompat.START)
    }

    fun onClickOther(name:String?){
        name?.let {
            when(it){
                "应用设置"->{

                }
                "工程模式"->{
                    startActivity(Intent(this@MainActivity, DebugActivity::class.java))
                }
            }
        }
        mBinding.drawer.closeDrawer(GravityCompat.START)
    }
}