package com.flynnchow.zero.magician.base.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.flynnchow.zero.common.util.StringUtils
import com.flynnchow.zero.magician.base.provider.MediaProvider

/// ->ML(ML_KIT-IMAGE_NET…)=>DATABASE=>LIVE_DATA
class MLSaveResultWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val srcData = MediaProvider.instance.getImageList()
        val inputData = srcData.filter {
            StringUtils.isEmpty(it.mlResultJson)
        }
        if (inputData.isNotEmpty()){

        }
        return Result.success()
    }
}