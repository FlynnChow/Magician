package com.flynnchow.zero.magician.debug

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.flynnchow.zero.baidu_sso.BaiduSsoActivity
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.common.helper.BaiduPanHelper
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ActivityDebugBinding


class DebugActivity : MagicianActivity<ActivityDebugBinding>(R.layout.activity_debug) {
    private val viewModel by lazy {
        getViewModel(DebugViewModel::class.java)
    }

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
        mBinding.clearAppConfig.setOnClickListener {
            val dao = RoomManager.instance.getAppConfigDao()
            startLaunch {
                dao.clear()
                showToast("已完成")
            }
        }
        mBinding.clearStore.setOnClickListener {
            val dao = RoomManager.instance.getStoreModelDao()
            startLaunch {
                dao.clear()
                showToast("已完成")
            }
        }
        mBinding.baidu.setOnClickListener {
            BaiduSsoActivity.launch(this, activityLaunch)
        }
        mBinding.upload.setOnClickListener {
            startLaunch({
                val uri = MediaProvider.instance.getImageList().first()
                BaiduPanHelper.uploadImage(uri)
            }, {
                LogDebug("测试",it.toString())
            })
        }
    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = viewModel
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }
}