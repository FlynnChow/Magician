package com.flynnchow.zero.common.proxy_api

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainCoroutineDispatcher
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface CoroutineProxy {
    fun startLaunch(block:suspend () -> Unit):Job

    fun startLaunch(block:suspend () -> Unit, error:((e: Throwable)->Unit)?, finally:(()->Unit)? = null):Job

    fun startLaunch(dispatcher: MainCoroutineDispatcher, block:suspend () -> Unit, error:((e: Throwable)->Unit)? = null, finally:(()->Unit)? = null):Job

    fun observerError():LiveData<Throwable>

    fun getCoroutine():CoroutineScope

    fun clearCoroutine()
}