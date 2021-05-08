package com.flynnchow.zero.magician.base.work

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.base.util.StringUtils
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.ml_kit.MLKitManager
import com.flynnchow.zero.model.MediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ImageAsyncWorker {
    data class Progress(
        var current: Int,
        val max: Int,
        val date: Long = System.currentTimeMillis(),
        var status: ProgressStatus = ProgressStatus.RUNNING
    )

    enum class ProgressStatus {
        RUNNING,
        SUCCESS
    }

    private fun Progress.change() {
        current++
        status = if (current < max)
            ProgressStatus.RUNNING
        else
            ProgressStatus.SUCCESS
    }

    companion object {
        val instance: ImageAsyncWorker by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageAsyncWorker()
        }
    }

    private var runing = false
    private val _progressData = MutableLiveData<Progress>()
    val progress: LiveData<Progress> = _progressData

    suspend fun doLaunchImageAsync() {
        if (runing) {
            return
        }
        val mlManager = MLKitManager.getInstance()
        val srcData = MediaProvider.instance.getImageList()
        val inputData = srcData.filter {
            StringUtils.isEmpty(it.mlResultJson)
        }
        val progress = Progress(0, inputData.size)
        runing = inputData.isNotEmpty()
        for (data in inputData) {
            mlManager.doAsync(data) {
                it?.apply {
                    updateResult(this, progress)
                }
            }
        }
        if (runing) {
            runing = false
        }
    }

    private fun updateResult(data: MediaModel, progress: Progress) {
        val mediaDao = RoomManager.instance.getMediaDao()
        GlobalScope.launch(Dispatchers.IO) {
            mediaDao.replaceInsert(data)
            launch(Dispatchers.Main) {
                progress.change()
                _progressData.value = progress
                MediaProvider.instance.updateMediaData(MediaProvider.MediaType.IMAGE, data)
            }
        }
    }
}