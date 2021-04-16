package com.flynnchow.zero.magician.launch

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ActivityLaunchBinding
import com.flynnchow.zero.magician.debug.DebugActivity
import com.flynnchow.zero.magician.main.MainActivity

class LaunchActivity : BindingActivity<ActivityLaunchBinding>(R.layout.activity_launch) {
    override fun onInitView() {
        mBinding.gallery.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        mBinding.debug.setOnClickListener {
            startActivity(Intent(this, DebugActivity::class.java))
        }
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        if (checkPermission(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ))){
            MediaProvider.instance.doWork()
        }
    }
}