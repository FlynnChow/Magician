@file:Suppress("NOTHING_TO_INLINE")

package com.flynnchow.zero.base.helper

import android.util.Log
import java.lang.StringBuilder

fun LogDebug(vararg logs: String?) {
    Log.d(getTag(), getLogString(*logs))
}

fun LogInfo(vararg logs: String?) {
    Log.i(getTag(), getLogString(*logs))
}

fun LogError(vararg logs: String?) {
    Log.e(getTag(), getLogString(*logs))
}

fun LogWarn(vararg logs: String?) {
    Log.w(getTag(), getLogString(*logs))
}

fun LogVerbose(vararg logs: String?) {
    Log.v(getTag(), getLogString(*logs))
}

fun LogDebug(vararg logs: Any?){
    Log.d(getTag(), getLogString(*logs))
}

fun LogInfo(vararg logs: Any?){
    Log.i(getTag(), getLogString(*logs))
}

fun LogError(vararg logs: Any?){
    Log.e(getTag(), getLogString(*logs))
}

fun LogWarn(vararg logs: Any?){
    Log.w(getTag(), getLogString(*logs))
}

fun LogVerbose(vararg logs: Any?){
    Log.v(getTag(), getLogString(*logs))
}

inline fun getTag(): String {
    var clazzName = getClassName()
    clazzName = clazzName.substring(clazzName.lastIndexOf(".") + 1)
    return " 日志[$clazzName]"
}

inline fun getClassName(): String {
    return Exception().stackTrace[1].className
}

private fun getLogString(vararg logs: String?): String {
    val strBuilder = StringBuilder()
    for (str in logs) {
        val append: String = str ?: "null"
        if (strBuilder.toString().isNotEmpty()) {
            strBuilder.append("，")
        }
        strBuilder.append(append)
    }
    return strBuilder.toString()
}

private fun getLogString(vararg logs: Any?): String {
    val strBuilder = StringBuilder()
    for (str in logs) {
        val append: String = str.toString()
        if (strBuilder.toString().isNotEmpty()) {
            strBuilder.append("，")
        }
        strBuilder.append(append)
    }
    return strBuilder.toString()
}