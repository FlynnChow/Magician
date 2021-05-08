package com.flynnchow.zero.common.proxy_api

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface CoroutineProxy {
    fun startLaunch(block: suspend () -> Unit): Job

    fun startLaunch(
        block: suspend () -> Unit,
        error: (suspend(e: Throwable) -> Unit)?,
        finally: (suspend() -> Unit)? = null
    ): Job

    fun startLaunch(dispatcher: CoroutineDispatcher, block: suspend () -> Unit): Job

    fun startLaunch(
        dispatcher: CoroutineDispatcher,
        block: suspend () -> Unit,
        error: (suspend(e: Throwable) -> Unit)? = null,
        finally: (suspend() -> Unit)? = null
    ): Job

    fun observerError(): LiveData<Throwable>

    fun getCoroutine(): CoroutineScope

    fun clearCoroutine()
}