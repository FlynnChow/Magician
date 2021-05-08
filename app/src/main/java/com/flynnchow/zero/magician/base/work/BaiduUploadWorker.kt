package com.flynnchow.zero.magician.base.work

import android.content.Context
import androidx.work.*
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.base.util.StringUtils
import com.flynnchow.zero.common.helper.BaiduPanHelper
import com.flynnchow.zero.database.AppConfigHelper
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.base.provider.MediaProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/// ->ML(ML_KIT-IMAGE_NET…)=>DATABASE=>LIVE_DATA
class BaiduUploadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    companion object {
        fun doAutoUploadWork() {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            val uploadRequest =
                OneTimeWorkRequest.Builder(BaiduUploadWorker::class.java)
                    .setConstraints(constraints)
                    .addTag(BaiduPanHelper.BAIDU_PAN_WORK_TAG)
                    .build()
            WorkManager.getInstance(BaseApplication.instance).enqueueUniqueWork(
                BaiduPanHelper.BAIDU_WORK_NAME,
                ExistingWorkPolicy.KEEP, uploadRequest
            )
        }
    }

    override suspend fun doWork(): Result {
        LogDebug("上传文件","开始工作")
        withContext(Dispatchers.IO){
            val appConfig = AppConfigHelper.getAppConfig()
            if (appConfig.useAutoBackup && appConfig.baiduToken.isNotEmpty()) {
                val preUpImages = MediaProvider.instance.getImageList().filter {
                    !StringUtils.isNotEmpty(it.baiduMd5)
                }
                for (image in preUpImages) {
                    if (!AppConfigHelper.getAppConfig().useAutoBackup){
                        return@withContext
                    }
                    val response = BaiduPanHelper.uploadImage(image)
                    LogDebug("上传文件",response)
                    if (response != null){
                        val mediaDao = RoomManager.instance.getMediaDao()
                        val newImage = image.clone().apply {
                            baiduMd5 = response.md5
                            baiduFsId = response.fs_id
                        }
                        mediaDao.update(newImage)
                        withContext(Dispatchers.Main){
                            MediaProvider.instance.updateMediaData(MediaProvider.MediaType.IMAGE,newImage)
                        }
                    }
                }
            }
        }
        return Result.success()
    }
}