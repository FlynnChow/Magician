package com.flynnchow.zero.base.util

import android.net.Uri
import android.os.Environment
import com.flynnchow.zero.base.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest

object FileUtils {
    private const val ANDROID_RESOURCE = "android.resource://"

    object Video {
        const val MP4 = "mp4"
        const val MP4_MIME_TYPE = "video/mp4"
    }

    object Audio {
        const val MP3 = "mp3"
        const val MP4 = "m4a"
        const val WAV = "m4a"
        const val MP3_MIME_TYPE = "audio/mp3"
        const val MP4_MIME_TYPE = "audio/m4a"
        const val WAV_MIME_TYPE = "audio/x-wav"
    }

    object Image {
        const val JPEG = "jpeg"
        const val JPG = "jpg"
        const val JPEG_MIME_TYPE = "image/jpeg"
        const val JPG_MIME_TYPE = "image/jpg"
    }

    object MediaType {
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
    suspend fun obtainPathFromUri(
        uri: Uri,
        outputDir: String,
        fileName: String,
        fileType: String,
    ): String? {
        return withContext(Dispatchers.IO) {
            var result: String? = null
            val targetFilePath = "$outputDir${fileName}.${fileType}"
            mkdirs(outputDir)
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
    suspend fun copyFile(
        src: String,
        out: String,
    ): String? {
        return withContext(Dispatchers.IO) {
            if (!File(src).exists()) {
                null
            } else {
                val inStream: InputStream = FileInputStream(File(src))
                inStream.let { inputStream ->
                    val outputStream: OutputStream = FileOutputStream(out)
                    val bytes = ByteArray(1024 * 4)
                    var len = -1
                    try {
                        while ({ len = inputStream.read(bytes);len }() != -1)
                            outputStream.write(bytes, 0, len)
                    } catch (e: Exception) {
                    } finally {
                        inputStream.close()
                        outputStream.close()
                    }
                }
                out
            }
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
        if (!dir.exists()) {
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
        if (dir?.isFile == true){
            deleteFile(dir.absolutePath)
        }
        if (dir == null || !dir.exists() || !dir.isDirectory) return
        val fileList = dir.listFiles() ?: return
        for (file in fileList) {
            if (file.isFile) file.delete()
            else if (file.isDirectory) deleteDirWithFile(file) // 递规的方式删除文件夹
        }
        dir.delete()
    }

    @JvmStatic
    fun deleteDirWithFile(dir: String?) {
        dir?.run {
            deleteDirWithFile(File(dir))
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun splitImage(uri: Uri, outDir: String, splitSize: Int = 1024 * 1024 * 4): List<File> {
        val resultFiles = ArrayList<File>()
        val inStream: InputStream? =
            BaseApplication.instance.contentResolver.openInputStream(uri)
        if (inStream != null) {
            val outputDir = File(outDir)
            deleteDirWithFile(outputDir)
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            val buffer = ByteArray(splitSize)
            var index = 0
            var length: Int
            while (inStream.read(buffer).also { length = it } != -1) {
                val splitPath = "${outDir}tempFile${index++}.split"
                val raf = RandomAccessFile(splitPath, "rw")
                raf.write(buffer, 0, length)
                raf.close()
                resultFiles.add((File(splitPath)))
            }
            inStream.close()
        }
        return resultFiles
    }

    fun splitImageMd5(
        uri: Uri,
        outDir: String,
        splitSize: Int = 1024 * 1024 * 4
    ): List<Pair<String, File>> {
        val outFileArray = splitImage(uri, outDir, splitSize)
        return outFileArray.map {
            Pair(it.getMd5() ?: "", it)
        }
    }

    fun File.getMd5(): String? {
        if (!exists() || !isFile) {
            return null
        }
        var digest: MessageDigest? = null
        var inStream: FileInputStream? = null
        val buffer = ByteArray(1024)
        var length = 0
        try {
            digest = MessageDigest.getInstance("MD5")
            inStream = FileInputStream(this)
            while (inStream.read(buffer).also { length = it } != -1) {
                digest.update(buffer, 0, length)
            }
            val bigInt = BigInteger(1, digest.digest())
            return bigInt.toString(16)
        } catch (e: IOException) {
            return null
        } finally {
            inStream?.close()
        }
    }

    @Throws(IOException::class)
    fun readFileToCharArray(file: File): CharArray{
        val fileData = StringBuilder(1000)
        val reader = BufferedReader(FileReader(file.absolutePath))
        val buf = CharArray(1024)
        var length:Int
        while (reader.read(buf).also { length = it } != -1) {
            val readData = String(buf, 0, length)
            fileData.append(readData)
        }
        reader.close()
        return fileData.toString().toCharArray()
    }

    @JvmStatic
    fun getUriFromRes(resId: Int): Uri {
        return Uri.parse(
            ANDROID_RESOURCE + BaseApplication.instance.packageName.toString() + File.separator + resId
        )
    }

    @JvmStatic
    fun getKBFromByte(size: Double): Double {
        return size / 1024
    }

    @JvmStatic
    fun getMBFromByte(size: Double): Double {
        return size / 1024 / 1024
    }

    @JvmStatic
    fun getGBFromByte(size: Double): Double {
        return size / 1024 / 1024 / 1024
    }

    enum class DirType {
        MOVIES,//视频
        PICTURE,//图片
        DOWNLOADS,//下载
        MUSIC,//音乐
        OTHER//其他
    }
}