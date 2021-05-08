package com.flynnchow.zero.common

import com.flynnchow.zero.base.BaseApplication
import com.hjq.toast.ToastUtils


abstract class CommonApplication:BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
    }
}