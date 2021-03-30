package com.flynnchow.zero.common.proxy_impl

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.proxy_api.CoroutineProxy
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CoroutineProxyImpl : CoroutineProxy, LifecycleObserver {
    private val _error:MutableLiveData<Throwable> = MutableLiveData()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun startLaunch(
        block: suspend () -> Unit,
        error: ((e: Throwable) -> Unit)?,
        finally: (() -> Unit)?
    ): Job {
        return startLaunch(Dispatchers.Main,block,error,finally)
    }

    override fun startLaunch(
        dispatcher: MainCoroutineDispatcher,
        block: suspend () -> Unit,
        error: ((e: Throwable) -> Unit)?,
        finally: (() -> Unit)?
    ): Job {
        return coroutineScope.launch {
            try {
                block.invoke()
            }catch (e:Exception){
                if(error != null){
                    error.invoke(e)
                }else{
                    _error.value = e
                }
            }finally {
                finally?.invoke()
            }
        }
    }

    override fun observerError(): LiveData<Throwable> {
        return _error
    }

    override fun getCoroutine(): CoroutineScope {
        return coroutineScope
    }

    override fun clearCoroutine() {
        coroutineScope.cancel()
    }
}