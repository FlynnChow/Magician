package com.flynnchow.zero.common.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.annotation.RawRes
import com.flynnchow.zero.base.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object FileUtils {
    private const val ANDROID_RESOURCE = "android.resource://"
    object Video{
        const val MP4 = "mp4"
        const val MP4_MIME_TYPE = "video/mp4"
    }

    object Audio{
        const val MP3 = "mp3"
        const val MP4 = "m4a"
        const val WAV = "m4a"
        const val MP3_MIME_TYPE = "audio/mp3"
        const val MP4_MIME_TYPE = "audio/m4a"
        const val WAV_MIME_TYPE = "audio/x-wav"
    }

    object Image{
        const val JPEG = "jpeg"
        const val JPG = "jpg"
        const val JPEG_MIME_TYPE = "image/jpeg"
        const val JPG_MIME_TYPE = "image/jpg"
    }

    object MediaType{
        const val images = "images"
        const val video = "video"
        const val audio = "audio"
    }

    @JvmStatic
    suspend fun obtainPathFromUri(
        uri: Uri,
        type: DirType,
        fileName: String,
        fileType: String,
        dirName: String
    ): String? {
        return withContext(Dispatchers.IO) {
            var result: String? = null
            val targetDirPath = getDirPath(type, dirName)
            val targetFilePath = "${targetDirPath}${fileName}.${fileType}"
            mkdirs(targetDirPath)
            deleteFile(targetFilePath)
            val inStream: InputStream? =
                BaseApplication.instance.contentResolver.openInputStream(uri)
            inStream?.let { inputStream ->
                val outputStream: OutputStream = FileOutputStream(targetFilePath)
                val bytes = ByteArray(1024 * 4)
                var len = -1
                try {
                    while ({ len = inputStream.read(bytes);len }() != -1)
                        outputStream.write(bytes, 0, len)
                    result = targetFilePath
                } catch (e: Exception) {
                } finally {
                    inputStream.close()
                    outputStream.close()
                }
            }
            result
        }
    }

    @JvmStatic
    fun getDirType(type: DirType): String {
        return when (type) {
            DirType.MOVIES -> Environment.DIRECTORY_MOVIES
            DirType.PICTURE -> Environment.DIRECTORY_PICTURES
            DirType.DOWNLOADS -> Environment.DIRECTORY_DOWNLOADS
            DirType.MUSIC -> Environment.DIRECTORY_MUSIC
            else -> Environment.DIRECTORY_DCIM
        }
    }

    @JvmStatic
    fun getDirPath(type: DirType, isCache: Boolean = true): String {
        var path =
            BaseApplication.instance.getExternalFilesDir(getDirType(type))?.absolutePath + File.separator
        if (isCache)
            path = "${path}cache${File.separator}"
        return path
    }

    @JvmStatic
    fun getDirPath(type: DirType, fileName: String, isCache: Boolean = true): String {
        return "${getDirPath(type, isCache)}${fileName}"
    }

    @JvmStatic
    fun mkdirs(dir: String) {
        val dir = File(dir)
        if (dir.isDirectory && !dir.exists()) {
            dir.mkdirs()
        }
    }

    @JvmStatic
    fun deleteFile(path: String) {
        val path = File(path)
        if (path.isFile && path.exists()) {
            path.delete()
        }
    }

    @JvmStatic
    fun deleteDirWithFile(dir: File?) {
        if (dir == null || !dir.exists() || !dir.isDirectory) return
        val fileList = dir.listFiles()?:return
        for (file in fileList) {
            if (file.isFile) file.delete()
            else if (file.isDirectory) deleteDirWithFile(file) // 递规的方式删除文件夹
        }
        dir.delete()
    }

    @JvmStatic
    fun deleteDirWithFile(dir: String?) {
        dir?.run {
            deleteDirWithFile(this)
        }
    }

    @JvmStatic
    fun getUriFromRes(resId: Int): Uri {
        return Uri.parse(
            ANDROID_RESOURCE + BaseApplication.instance.packageName.toString() + File.separator + resId
        )
    }

    @JvmStatic
    fun getKBFromByte(size: Double):Double{
        return size/1024
    }

    @JvmStatic
    fun getMBFromByte(size: Double):Double{
        return size/1024/1024
    }

    @JvmStatic
    fun getGBFromByte(size: Double):Double{
        return size/1024/1024/1024
    }

    enum class DirType {
        MOVIES,//视频
        PICTURE,//图片
        DOWNLOADS,//下载
        MUSIC,//音乐
        OTHER//其他
    }
}