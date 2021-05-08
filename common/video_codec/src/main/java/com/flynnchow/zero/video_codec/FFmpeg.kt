package com.flynnchow.zero.video_codec

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.base.util.FileUtils
import com.flynnchow.zero.model.StoreVideo
import io.microshow.rxffmpeg.RxFFmpegInvoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import kotlin.math.roundToInt

class FFmpeg(context: Context, private val config: FFmpegHelper.FFmpegConfig) :
    RxFFmpegInvoke.IFFmpegListener {
    companion object {
        private var isRunning = false
        private var progressValue: FFTaskProgress? = null
        private val _progress = MutableLiveData<FFTaskProgress?>()
        val ffProgress: LiveData<FFTaskProgress?> = _progress
    }

    private val inputImageCache: String =
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator + "cache" + File.separator + "cache_image_in" + File.separator
    private val inputAudioCache: String =
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath + File.separator + "cache" + File.separator + "cache_audio_in" + File.separator
    private val inputVideoCache: String =
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath + File.separator + "cache" + File.separator + "cache_video_in" + File.separator
    private val outputVideoCache: String =
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath + File.separator + "cache" + File.separator + "cache_video_in" + File.separator
    private val outputImageCache: String =
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator + "cache" + File.separator + "cache_image_in" + File.separator
    private val outputAudioCache: String =
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath + File.separator + "cache" + File.separator + "cache_audio_in" + File.separator
    private val outPutPath: String =
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath + File.separator + "result" + File.separator + "magician_video" + File.separator

    private var maxProgressTime: Float = 1f

    suspend fun run(): StoreVideo = withContext(Dispatchers.IO) {
        resetFFmpeg()
        val result = try {
            config.uris?.let { uris ->
                clearDir()
                val copyImagePaths = copyImage(uris)
                val audioPath = copyAudio(config.audio)
                withContext(Dispatchers.Default) {
                    val inputImagePaths = initImages(copyImagePaths)
                    val video = imageToTransitionVideo(inputImagePaths, audioPath)
                    if (config.effect == FFmpegHelper.FFmpegConfig.NONE) {
                        updateProgress(hideProgress = true, message = "正在保存影集")
                        val result =
                            FileUtils.copyFile(
                                video,
                                "${outPutPath}${System.currentTimeMillis()}.mp4"
                            )
                        StoreVideo(result ?: "", uris.size * 4)
                    } else {
                        val result = videoToEffect(video)
                        StoreVideo(result, uris.size * 4)
                    }
                }
            }
        } catch (e: Exception) {
            if (e is CancelEvent) {
                throw e
            } else {
                throw Exception("制作视频失败")
            }
        }
        try {
            if (result == null) {
                throw Exception("制作视频失败")
            }
            if (!isRunning) {
                FileUtils.deleteFile(result.path)
                throw CancelEvent("取消制作影集成功")
            }
            result
        } catch (e: Exception) {
            throw e
        } finally {
            clearDir()
            _progress.postValue(null)
        }
    }

    private suspend fun resetFFmpeg() {
        isRunning = true
        var taskCount = 1
        if (config.audio != null) {
            taskCount += 1
        }
        if (config.effect != FFmpegHelper.FFmpegConfig.NONE) {
            taskCount += 1
        }
        withContext(Dispatchers.Main) {
            progressValue = FFTaskProgress(
                0,
                0,
                taskCount,
                "正在初始化",
                true
            )
            _progress.value = progressValue
        }
    }

    private fun updateProgress(
        hideProgress: Boolean? = null,
        progress: Int? = null,
        currentTask: Int? = null,
        totalTask: Int? = null,
        message: String? = null,
        nextTask: Boolean? = null
    ) {
        progressValue = _progress.value?.updateWith(
            hideProgress,
            if (nextTask == true) 0 else progress,
            if (nextTask == true) (_progress.value?.currentTask ?: 0) + 1 else currentTask,
            totalTask,
            message
        )
        _progress.postValue(progressValue)
    }

    private suspend fun copyImage(uris: List<Uri>): List<String> {
        checkRunningState()
        updateProgress(message = "正在复制照片文件")
        val imageCache = ArrayList<String>()
        var fileName = 0
        for (uri in uris) {
            val path =
                FileUtils.obtainPathFromUri(uri, inputImageCache, "image_copy${fileName}", "jpeg")
            path?.run {
                fileName++
                imageCache.add(this)
            }
        }
        return imageCache
    }

    private suspend fun copyAudio(uri: Uri?): String? {
        checkRunningState()
        updateProgress(message = "正在复制音频文件")
        uri?.let {
            val path = FileUtils.obtainPathFromUri(it, inputAudioCache, "audio", "mp3")
            return path
        }
        return null
    }

    private suspend fun initImages(paths: List<String>): List<String> {
        checkRunningState()
        updateProgress(message = "正在对照片进行预处理")
        val commands = ArrayList<String>()
        val result = ArrayList<String>()
        for ((fileName, path) in paths.withIndex()) {
            val outPath = "${inputImageCache}image${fileName}.jpeg"
            commands.clear()
            commands.add("ffmpeg")
            commands.add("-i")
            commands.add(path)
            commands.add("-vf")
//            if (config.effect == EffectProvider.Effect_0 || config.effect == EffectProvider.Effect_1) {
//                commands.add("setsar=sar=1/1[a];[a]scale=1080:1080*ih/iw[c];[c]crop=1080:1080")
//            } else {
//                commands.add("setsar=sar=1/1[a],[a]scale=1080:1080*ih/iw,pad=1080:1920:0:960:black")
//            }
            commands.add("setsar=sar=72/72")
            commands.add(outPath)
            result.add(outPath)
            RxFFmpegInvoke.getInstance().runCommand(commands.toTypedArray(), this@FFmpeg)
        }
        return result
    }

    private suspend fun imageToTransitionVideo(images: List<String>, audio: String?): String {
        checkRunningState()
        updateProgress(nextTask = true, message = "正在将照片编码为视频")
        maxProgressTime = images.size * 4 * 1000000f
        val videoPath = withContext(Dispatchers.Default) {
            val videoResult = "${outputVideoCache}image_to_video.mp4"
            val commands = ArrayList<String>()
            commands.add("ffmpeg")
            for (path in images) {
                commands.add("-i")
                commands.add(path)
            }
            commands.add("-filter_complex")
            val filterBuild = StringBuilder()
            for (index in images.indices) {
                filterBuild.append(getFilter(index))
            }
            for (index in images.indices) {
                filterBuild.append("[v$index]")
            }
            filterBuild.append("concat=n=${images.size}:v=1:a=0,format=yuv420p[v]")
            commands.add(filterBuild.toString())
            commands.add("-map")
            commands.add("[v]")
            commands.add("-r")
            commands.add("25")
            commands.add("-t")
            commands.add("${4 * images.size}")
            commands.add("-aspect")
            commands.add("9:16")
            commands.add(videoResult)
            RxFFmpegInvoke.getInstance().runCommand(commands.toTypedArray(), this@FFmpeg)
            videoResult
        }
        return if (audio != null) {
            mergeVideoAndAudio(videoPath, audio, "${4 * images.size}")
        } else {
            videoPath
        }
    }

    private suspend fun mergeVideoAndAudio(video: String, audio: String, duration: String): String {
        checkRunningState()
        updateProgress(nextTask = true, message = "正在往视频中合成音频")
        val videoResult = "${outputVideoCache}image_to_video_audio.mp4"
        val commands = ArrayList<String>()
        commands.add("ffmpeg")
        commands.add("-i")
        commands.add(video)
        commands.add("-i")
        commands.add(audio)
        commands.add("-t")
        commands.add(duration)
        commands.add(videoResult)
        RxFFmpegInvoke.getInstance().runCommand(commands.toTypedArray(), this@FFmpeg)
        return videoResult
    }

    private suspend fun videoToEffect(video: String): String {
        checkRunningState()
        updateProgress(nextTask = true, message = "正在为视频合成特效")
        val result = "${outPutPath}${System.currentTimeMillis()}.mp4"
        val commands = ArrayList<String>()
        commands.add("ffmpeg")
        commands.add("-i")
        commands.add(video)
        commands.add("-vf")
        commands.add(getEffect())
        commands.add("-aspect")
        commands.add("9:16")
        commands.add("-f")
        commands.add("mp4")
        commands.add(result)
        commands.add("-y")
        RxFFmpegInvoke.getInstance().runCommand(commands.toTypedArray(), this@FFmpeg)
        return result
    }

    private fun clearDir() {
        clearDir(inputImageCache)
        clearDir(inputAudioCache)
        clearDir(inputVideoCache)
        clearDir(outputVideoCache)
        clearDir(outputImageCache)
        clearDir(outputAudioCache)
        if (!File(outPutPath).exists()) {
            File(outPutPath).mkdirs()
        }
    }

    private fun clearDir(path: String) {
        val dir = File(path)
        FileUtils.deleteDirWithFile(dir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private fun getFilter(index: Int): String {
        val zoom = when (config.zoom) {
            ZoomProvider.zoom_add ->
                "zoompan=z='min(zoom+0.0025,1.5)'"

            ZoomProvider.zoom_sub ->
                "zoompan=z='if(gt(on,1),zoom-0.0025,1.5)'"

            else -> {
                if (config.pan == PanProvider.none)
                    "zoompan=z=1.0"
                else
                    "zoompan=z=1.5"
            }
        }
        val pan = when (config.pan) {
            PanProvider.translation_x_add ->
                "x='if(lte(on,-1),(iw-iw/zoom)/2,x-3)':y='if(lte(on,1),(ih-ih/zoom)/2,y)'"
            PanProvider.translation_x_sub ->
                "x='if(lte(on,1),(iw/zoom)/2,x+3)':y='if(lte(on,1),(ih-ih/zoom)/2,y)'"
            PanProvider.translation_y_add ->
                "x='if(lte(on,1),(iw-iw/zoom)/2,x)':y='if(lte(on,-1),(ih-ih/zoom)/2,y-2)'"
            PanProvider.translation_y_sub ->
                "x='if(lte(on,1),(iw-iw/zoom)/2,x)':y='if(lte(on,1),(ih/zoom)/2,y+2)'"
            PanProvider.translation_x_add_y_add ->
                "x='if(lte(on,1),(iw-iw/zoom)/2,x-3)':y='if(lte(on,1),(ih/zoom)/2,y-2)'"
            PanProvider.translation_x_add_y_sub ->
                "x='if(lte(on,1),(iw-iw/zoom)/2,x-3)':y='if(lte(on,1),(ih/zoom)/2,y+2)'"
            PanProvider.translation_x_sub_y_add ->
                "x='if(lte(on,1),(iw-iw/zoom)/2,x+3)':y='if(lte(on,1),(ih/zoom)/2,y-2)'"
            PanProvider.translation_x_sub_y_sub ->
                "x='if(lte(on,1),(iw-iw/zoom)/2,x+3)':y='if(lte(on,1),(ih/zoom)/2,y+2)'"
            else ->
                "x=x:y=y"

        }
        val transition = when (config.transition) {
            TransitionProvider.fade ->
                "[fade];[fade]fade=in:0:20,fade=out:80:20"
            else ->
                ""
        }
        val size = "s=1280x720"
        return "[$index:v]${zoom}:${pan}:${size}:fps=25:d=100${transition}[v$index];"
    }

    private fun getEffect(): String {
        return when (config.effect) {
            EffectProvider.effect_0 -> {
                "split[a][b];[a]scale=1080:1920[1];[b]scale=1080:1080[2];[1][2]overlay=0:(H-h)/2"
            }
            EffectProvider.effect_1 -> {
                "split[a][b];[a]scale=1080:1920,boxblur=10:5[1];[b]scale=1080:1080[2];[1][2]overlay=0:(H-h)/2"
            }
            EffectProvider.effect_2 -> {
                "split[main][a];[a]crop=iw/2:ih:0:0, hflip[flip];[main][flip]overlay=W/2:0"
            }
            EffectProvider.effect_3 -> {
                "split[main][a];[a]crop=iw:ih/2:0:0, vflip[flip];[main][flip]overlay=0:H/2"
            }
            else -> {
                "scale=1080:1920"
            }
        }
    }

    private fun checkRunningState() {
        if (!isRunning) {
            throw CancelEvent("取消制作影集成功")
        }
    }

    fun cancel() {
        isRunning = false
        RxFFmpegInvoke.getInstance().exit()
    }

    fun isWorking() = isRunning

    override fun onFinish() {

    }

    override fun onProgress(progress: Int, progressTime: Long) {
        val nextProgress =
            ((progressTime / (if (maxProgressTime > 0) maxProgressTime else 1f)) * 100f).roundToInt()
        if (nextProgress in 0..100) {
            updateProgress(progress = nextProgress)
        }
    }

    override fun onCancel() {

    }

    override fun onError(message: String?) {

    }
}