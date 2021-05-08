package com.flynnchow.zero.magician.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.AppConfigHelper
import com.flynnchow.zero.magician.base.work.BaiduUploadWorker
import com.flynnchow.zero.model.BackupExpiredMessage
import kotlinx.coroutines.Dispatchers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SettingViewModel : BaseViewModel() {
    var initializerFinish = false
    val checkedBackup = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _requestBaiduSso = MutableLiveData<Boolean>()
    val requestBaiduSso: LiveData<Boolean> = _requestBaiduSso

    init {
        EventBus.getDefault().register(this)
        startLaunch(Dispatchers.Main) {
            var appConfig = AppConfigHelper.getAppConfig()
            if (appConfig.useAutoBackup && appConfig.baiduToken.isEmpty()) {
                appConfig = appConfig.copy(useAutoBackup = false)
                AppConfigHelper.updateAppConfig(appConfig)
            }
            checkedBackup.value = appConfig.useAutoBackup
            initializerFinish = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    fun checkedChange(checked: Boolean) {
        startLaunch({
            val appConfig = AppConfigHelper.getAppConfig()
            if (checked && appConfig.baiduToken.isEmpty()) {
                _loading.value = true
                _requestBaiduSso.value = true
            }else if (checked && appConfig.baiduToken.isNotEmpty()){
                BaiduUploadWorker.doAutoUploadWork()
            }
            if (appConfig.useAutoBackup != checked) {
                AppConfigHelper.updateAppConfig(
                    appConfig.copy(
                        useAutoBackup = checked
                    )
                )
            }
        }, {}, {
            _loading.value = false
        })
    }

    fun onUpdateToken(assetsToken: String?) {
        startLaunch({
            if (assetsToken?.isNotEmpty() == true) {
                _loading.value = true
                val appConfig = AppConfigHelper.getAppConfig()
                AppConfigHelper.updateAppConfig(
                    appConfig.copy(
                        baiduToken = assetsToken,
                        useAutoBackup = true
                    )
                )
            } else {
                checkedBackup.value = false
                showToast("授权失败，无法自动备份照片")
            }
        }, {}, {
            _loading.value = false
        })
    }

    @Subscribe
    fun BackupTokenExpired(message: BackupExpiredMessage) {
        checkedBackup.value = false
    }
}