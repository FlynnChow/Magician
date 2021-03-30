@file:Suppress("NOTHING_TO_INLINE")

package com.flynnchow.zero.common.helper

import android.util.Log

fun logDebug(log:String){
    Log.d(getTag(),log)
}

fun logInfo(log:String){
    Log.i(getTag(),log)
}

fun logError(log:String){
    Log.e(getTag(),log)
}

fun logWarn(log:String){
    Log.w(getTag(),log)
}

fun logVerbose(log:String){
    Log.v(getTag(),log)
}

inline fun getTag():String{
    var clazzName = getClassName()
    clazzName = clazzName.substring(clazzName.lastIndexOf(".") + 1)
    return " 日志[$clazzName]"
}

inline fun getClassName():String{
    return Exception().stackTrace[1].className
}