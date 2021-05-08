package com.flynnchow.zero.net.provider

import com.flynnchow.zero.net.api.BaiduPanApi
import com.flynnchow.zero.net.api.BaiduPscApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteProvider {
    private const val BAIDU_PAN_URL = "https://pan.baidu.com/"
    private const val BAIDU_PCS_URL = "https://d.pcs.baidu.com/"

    @JvmStatic
    val okhttpClient: OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpClient.Builder().run {
            build()
        }
    }

    @JvmStatic
    private val baiduPanRetrofit: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder().run {
            baseUrl(BAIDU_PAN_URL)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            addConverterFactory(GsonConverterFactory.create())
            client(okhttpClient)
            build()
        }
    }

    @JvmStatic
    private val baiduPSCRetrofit: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder().run {
            baseUrl(BAIDU_PCS_URL)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            addConverterFactory(GsonConverterFactory.create())
            client(okhttpClient)
            build()
        }
    }

    @JvmStatic
    fun baiduPanService(): BaiduPanApi  = baiduPanRetrofit.create(BaiduPanApi::class.java)

    @JvmStatic
    fun baiduPscService(): BaiduPscApi  = baiduPSCRetrofit.create(BaiduPscApi::class.java)
}