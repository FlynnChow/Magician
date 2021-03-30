package com.flynnchow.zero.base

import android.app.Application
import androidx.annotation.CallSuper

abstract class BaseApplication:Application() {
    companion object{
        lateinit var instance:BaseApplication
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}