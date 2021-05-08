package com.flynnchow.zero.baidu_sso

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import com.flynnchow.zero.baidu_sso.databinding.ActivityBaiduSsoBinding
import com.flynnchow.zero.base.helper.LogDebug
import com.tencent.smtt.sdk.WebView
import com.ycbjie.webviewlib.client.JsX5WebViewClient
import com.ycbjie.webviewlib.view.X5WebView
import java.util.regex.Pattern

class BaiduSsoActivity : AppCompatActivity() {
    companion object {
        private const val app_id = "5XpNefnNutmMoFAzxIDyZWdGMiQ4mcDU"
        const val RESULT_OK = 0xbd0
        const val RESULT_FAIL = 0xbd1
        fun launch(context: Context, launcher: ActivityResultLauncher<Intent>) {
            launcher.launch(Intent(context, BaiduSsoActivity::class.java))
        }

        fun getToken(result: ActivityResult): String? {
            if (result.resultCode == RESULT_OK) {
                return result.data?.getStringExtra("token")
            }
            return null
        }
    }

    private lateinit var mBinding: ActivityBaiduSsoBinding
    private var webView: X5WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidu_sso)
        webView = mBinding.webView
        requestBaiduNumber()
    }

    private fun requestBaiduNumber() {
        val url =
            "https://openapi.baidu.com/oauth/2.0/authorize?response_type=token&client_id=${app_id}&redirect_uri=oob&scope=basic,netdisk&display=mobile"
        webView?.webViewClient = SsoWebViewClient(mBinding.webView, this)
        webView?.loadUrl(url)
    }

    private fun onCheckAuthorization(url: String) {
        LogDebug("测试",url)
        onBack(url.getParamsValue("access_token"))
    }

    private fun String.getParamsValue(param: String): String {
        val pattern = Pattern.compile("(?<=${param}=).+?(?=(&|\$))")
        val matcher = pattern.matcher(this)
        return if (matcher.find()) {
            matcher.group()
        } else {
            ""
        }
    }

    override fun onBackPressed() {
        onBack()
    }

    private fun onBack(token: String? = null) {
        val resultCode = if (token == null || token.isNotEmpty()) RESULT_OK else RESULT_FAIL
        val data = Intent()
        data.putExtra("token", token)
        setResult(resultCode, data)
        finish()
    }

    private inner class SsoWebViewClient(webView: X5WebView, context: Context) :
        JsX5WebViewClient(webView, context) {
        override fun onPageStarted(webView: WebView?, url: String?, bitmap: Bitmap?) {
            super.onPageStarted(webView, url, bitmap)
            url?.run {
                if (url.startsWith("https://openapi.baidu.com/oauth/2.0/login_success#")) {
                    onCheckAuthorization(url)
                }
            }
        }
    }

    override fun onDestroy() {
        webView?.destroy()
        super.onDestroy()
    }
}