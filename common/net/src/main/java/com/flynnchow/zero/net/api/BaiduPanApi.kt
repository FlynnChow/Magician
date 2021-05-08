package com.flynnchow.zero.net.api

import com.flynnchow.zero.model.CreateResponse
import com.flynnchow.zero.model.ImageResponse
import com.flynnchow.zero.model.PreCreateResponse
import com.flynnchow.zero.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BaiduPanApi {
    @POST("rest/2.0/xpan/file?method=precreate")
    @FormUrlEncoded
    fun preCreate(
        @Query("access_token") accessToken: String,
        @FieldMap map: Map<String,String>
    ): Call<PreCreateResponse>

    @POST("rest/2.0/xpan/file?method=create")
    @FormUrlEncoded
    fun create(
        @Query("access_token") accessToken: String,
        @FieldMap map: Map<String,String>
    ): Call<CreateResponse>

    @GET("rest/2.0/xpan/file?method=list")
    fun getImageList(
        @Query("access_token") accessToken: String,
        @Query("dir") dir: String = "/apps/magician/照片备份/",
        @Query("order") order: String = "time",
        @Query("start") start: Int = 0,
        @Query("limit") limit: Int = 10000,
        @Query("web") web: String = "web",
        @Query("web") folder: Int = 0,
        @Query("desc") desc: Int = 0,
    ): Call<ImageResponse>
}