package com.flynnchow.zero.common.helper

import android.os.Environment
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.base.util.FileUtils
import com.flynnchow.zero.database.AppConfigHelper
import com.flynnchow.zero.model.*
import com.flynnchow.zero.net.provider.RemoteProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.await
import java.io.File
import java.lang.Exception


object BaiduPanHelper {
    const val BAIDU_PAN_WORK_TAG = "BAIDU_PAN_WORK_TAG"
    const val BAIDU_WORK_NAME = "BAIDU_WORK_NAME"

    private val baiduCache by lazy {
        BaseApplication.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator + "cache" + File.separator + "baidu_pan" + File.separator
    }

    suspend fun uploadImage(image: MediaModel): CreateResponse? {
        val appConfig = AppConfigHelper.getAppConfig()
        if (!appConfig.useAutoBackup || appConfig.baiduToken.isEmpty()) {
            return null
        }
        return withContext(Dispatchers.IO) {
            val tempDir =
                "${baiduCache}${File.separator}${System.currentTimeMillis()}${File.separator}"
            val md2File = FileUtils.splitImageMd5(image.getUri(), tempDir)
            val md5List: List<String> = md2File.map {
                it.first
            }
            val path = "/apps/magician/照片备份/${image.name}"
            val size = image.size.toString()
            val isdir = "0"
            val autoinit = "1"
            val block_list = Gson().toJson(md5List)

            val preResponse =
                RemoteProvider.baiduPanService()
                    .preCreate(appConfig.baiduToken, HashMap<String, String>().apply {
                        put("path", path)
                        put("size", size)
                        put("isdir", isdir)
                        put("autoinit", autoinit)
                        put("block_list", block_list)
                    })
                    .await()
            if (preResponse.errno == 0) {
                uploadImage(appConfig.baiduToken, path, size, tempDir, md2File, preResponse)
            } else {
                null
            }
        }
    }

    private suspend fun uploadImage(
        token: String, path: String, size: String, cachePath: String,
        md2File: List<Pair<String, File>>,
        preResponse: PreCreateResponse
    ): CreateResponse? {
        val blockList = ArrayList<String>()
        for (id in preResponse.block_list ?: ArrayList()) {
            val requestFile = md2File[id].second
                .asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", path, requestFile)
            val uploadResponse =
                RemoteProvider.baiduPscService()
                    .upload(token, path, preResponse.uploadid, id, "tmpfile", part)
                    .await()
            if (uploadResponse.errno == 0) {
                blockList.add(uploadResponse.md5)
            } else {
                throw Exception("upload error")
            }
        }
        return createImage(
            token,
            path,
            size,
            cachePath,
            preResponse.uploadid,
            Gson().toJson(blockList)
        )
    }

    private suspend fun createImage(
        token: String,
        path: String,
        size: String,
        cachePath: String,
        uploadid: String,
        block_list: String
    ): CreateResponse? {
        val createResponse =
            RemoteProvider.baiduPanService().create(token, HashMap<String, String>().apply {
                put("path", path)
                put("size", size)
                put("isdir", "0")
                put("rtype", "1")
                put("uploadid", uploadid)
                put("block_list", block_list)
            }).await()
        FileUtils.deleteDirWithFile(cachePath)
        return if (createResponse.errno == 0) {
            createResponse
        } else {
            null
        }
    }

    suspend fun getBaiduPanImages():List<BaiduPanImage>?{
        val appConfig = AppConfigHelper.getAppConfig()
        return RemoteProvider.baiduPanService().getImageList(appConfig.baiduToken).await().list
    }
}