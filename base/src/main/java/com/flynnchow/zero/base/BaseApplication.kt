package com.flynnchow.zero.base

import android.app.Application
import androidx.annotation.CallSuper
import com.ycbjie.webviewlib.utils.X5WebUtils

abstract class BaseApplication:Application() {
    companion object{
        lateinit var instance:BaseApplication
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        instance = this
        X5WebUtils.init(this)
    }
}