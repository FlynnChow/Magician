package com.flynnchow.zero.video_codec

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.base.util.FileUtils
import com.flynnchow.zero.model.StoreVideo
import io.microshow.rxffmpeg.RxFFmpegInvoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Error
import kotlin.math.max
import kotlin.math.min

data class FFTaskProgress(
    var progress: Int,
    var currentTask: Int,
    var totalTask: Int,
    var message: String,
    var hideProgress:Boolean
) {
    fun updateWith(
        hideProgress: Boolean? = null,
        progress: Int? = null,
        currentTask: Int? = null,
        totalTask: Int? = null,
        message: String? = null
    ): FFTaskProgress {
        hideProgress?.run {
            this@FFTaskProgress.hideProgress = this
        }
        progress?.run {
            this@FFTaskProgress.hideProgress = false
            this@FFTaskProgress.progress = this
        }
        currentTask?.run {
            this@FFTaskProgress.currentTask = this
        }
        totalTask?.run {
            this@FFTaskProgress.totalTask = this
        }
        message?.run {
            this@FFTaskProgress.message = this
        }
        return this
    }
}