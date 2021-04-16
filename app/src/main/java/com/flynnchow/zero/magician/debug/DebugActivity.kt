package com.flynnchow.zero.magician.debug

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.common.helper.ActivityHelper
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityDebugBinding
import kotlin.system.exitProcess


class DebugActivity : BindingActivity<ActivityDebugBinding>(R.layout.activity_debug) {
    override fun onInitView() {
        mBinding.clearMlCache.setOnClickListener {
            val dao = RoomManager.instance.getMediaDao()
            startLaunch {
                dao.clear()
                showToast("已完成")
            }
        }

        mBinding.clearLabelCache.setOnClickListener {
            val dao = RoomManager.instance.getMlAlbumDao()
            startLaunch {
                dao.clear()
                showToast("已完成")
            }
        }

        mBinding.clearAppConfig.setOnClickListener {
            val dao = RoomManager.instance.getAppConfigDao()
            startLaunch {
                dao.clear()
                showToast("已完成")
            }
        }

        mBinding.resetApp.setOnClickListener {
            restartApp()
            showToast("已重启")
        }
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }

    private fun restartApp() {
        exitProcess(2)
    }
}