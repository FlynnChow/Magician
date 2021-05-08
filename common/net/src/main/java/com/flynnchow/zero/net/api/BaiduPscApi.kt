package com.flynnchow.zero.net.api

import com.flynnchow.zero.model.PreCreateResponse
import com.flynnchow.zero.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BaiduPscApi {
    @Multipart
    @POST("rest/2.0/pcs/superfile2?method=upload")
    @Headers("accept: */*")
    fun upload(
        @Query("access_token") accessToken: String,
        @Query("path") path: String,
        @Query("uploadid") uploadid: String = "upload",
        @Query("partseq") partseq: Int,
        @Query("type") type: String,
        @Part part: MultipartBody.Part,
    ): Call<UploadResponse>

    @POST("rest/2.0/xpan/file")
    fun create(
        @Query("access_token") accessToken: String,
        @Body body: RequestBody,
        @Query("method") method: String = "create"
    ): Call<PreCreateResponse>
}