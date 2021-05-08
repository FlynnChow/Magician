package com.flynnchow.zero.magician.launch

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.helper.TitleFontHelper
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ActivityLaunchBinding
import com.flynnchow.zero.magician.main.MainActivity
import kotlinx.coroutines.delay

class LaunchActivity : BindingActivity<ActivityLaunchBinding>(R.layout.activity_launch) {
    override fun onInitView() {

    }

    override fun onInitData(savedInstanceState: Bundle?) {
        if (checkPermission(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ))){
            MediaProvider.instance.doWork()
        }
        startLaunchApp()
    }

    private fun startLaunchApp(){
        startLaunch {
            delay(1000)
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        TitleFontHelper.updateTitleFont(mBinding.name)
    }

    override fun onCreateBefore() {
        super.onCreateBefore()
    }
}