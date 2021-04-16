package com.flynnchow.zero.common.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.proxy_api.CoroutineProxy
import com.flynnchow.zero.common.helper.ActivityHelper
import com.flynnchow.zero.common.proxy_impl.CoroutineProxyImpl
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.hjq.toast.ToastUtils
import com.hw.ycshareelement.YcShareElement

abstract class BaseActivity(@LayoutRes private val resId: Int?) : AppCompatActivity(),
    CoroutineProxy by CoroutineProxyImpl() {
    companion object {
        @JvmStatic
        fun get(activity: Activity, block: (() -> Unit)? = null): BaseActivity? {
            if (activity is BaseActivity)
                return activity
            block?.invoke()
            return null
        }

        private const val requestPermissionCode = 0x01
    }

    private lateinit var activityProxy: ActivityHelper

    private var permissionCallback:((result:Boolean,permissions:List<String>)->Unit)? = null

    final override fun onCreate(savedInstanceState: Bundle?) {
        activityProxy = ActivityHelper(this, lifecycle)
        onCreateBefore()
        super.onCreate(savedInstanceState)
        resId?.run { createView(this) }
        onRootViewCreated()
        onInitView()
        onInitData(savedInstanceState)
        onInitObserver()
    }

    @CallSuper
    protected open fun onCreateBefore() {
        YcShareElement.enableContentTransition(application)
    }

    protected open fun createView(@LayoutRes resId: Int) {
        setContentView(resId)
    }

    @CallSuper
    protected open fun onRootViewCreated(){}

    protected abstract fun onInitView()

    protected abstract fun onInitData(savedInstanceState: Bundle?)

    @CallSuper
    protected open fun onInitObserver() {
        observerError().observe(this, Observer {
            showToast(it.message)
        })
    }

    fun showToast(msg: Any?) {
        ToastUtils.show(msg.toString())
    }

    protected fun<VM:ViewModel> getViewModel(clazz:Class<VM>):VM{
        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[clazz]
        if(viewModel is BaseViewModel){
            viewModel.finish.observe(this, Observer {
                if(it) finish()
            })
            viewModel.backPress.observe(this, Observer {
                if(it) onBackPressed()
            })
            viewModel.toast.observe(this, Observer {
                showToast(it)
            })
        }
        return viewModel
    }

    fun requestPermissions(permissions: Array<String>,callback:((result:Boolean,permissions:List<String>)->Unit)? = null){
        val request = ArrayList<String>()
        for (permission in permissions){
            if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED)
                request.add(permission)
        }
        if(request.isNotEmpty()){
            permissionCallback = callback
            ActivityCompat.requestPermissions(this,request.toTypedArray(),requestPermissionCode)
        }else{
            callback?.invoke(true,request)
        }
    }

    fun requestPermissions(permissions: Array<String>,noNeedRequestCallback:(()->Unit)?,callback:((result:Boolean,permissions:List<String>)->Unit)? = null){
        if (checkPermission(permissions)){
            noNeedRequestCallback?.invoke()
            LogDebug("测试","12231")
            return
        }
        LogDebug("测试","122322")
        val request = ArrayList<String>()
        for (permission in permissions){
            if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED)
                request.add(permission)
        }
        if(request.isNotEmpty()){
            permissionCallback = callback
            ActivityCompat.requestPermissions(this,request.toTypedArray(),requestPermissionCode)
        }else{
            callback?.invoke(true,request)
        }
    }

    fun checkPermission(permissions: Array<String>):Boolean{
        for (permission in permissions){
            if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var result = true
        if(requestCode == requestPermissionCode){
            val permissionsResult = ArrayList<String>()
            for (index in grantResults.indices){
                val grant = grantResults[index]
                if(grant != PackageManager.PERMISSION_GRANTED){
                    result = false
                    permissionsResult.add(permissions[index])
                }
            }
            permissionCallback?.invoke(result,permissionsResult)
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        clearCoroutine()
    }
}