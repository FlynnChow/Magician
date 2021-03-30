package com.flynnchow.zero.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.flynnchow.zero.common.activity.BaseActivity
import com.flynnchow.zero.common.proxy_api.CoroutineProxy
import com.flynnchow.zero.common.proxy_impl.CoroutineProxyImpl
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.hjq.toast.ToastUtils
import java.lang.Exception

abstract class BaseFragment(@LayoutRes private val resId: Int?) : Fragment(),
    CoroutineProxy by CoroutineProxyImpl() {
    protected lateinit var mRootView: View

    private var isFirstInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore()
        super.onCreate(savedInstanceState)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mRootView = if(resId != null){
            createView(resId,container)
        }else{
            super.onCreateView(inflater, container, savedInstanceState)?: View(context)
        }
        return mRootView
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isFirstInit) isFirstInit = false
        onRootViewCreated()
        onInitView()
        onInitData(isFirstInit,savedInstanceState)
        onInitObserver()
    }

    @CallSuper
    protected fun onCreateBefore() {

    }

    protected open fun createView(@LayoutRes resId: Int, container: ViewGroup?):View {
        return layoutInflater.inflate(resId, container,false)
    }

    @CallSuper
    protected open fun onRootViewCreated(){}

    protected abstract fun onInitView()

    protected abstract fun onInitData(isFirst:Boolean,savedInstanceState: Bundle?)

    @CallSuper
    protected fun onInitObserver() {
        observerError().observe(this, Observer {
            toast(it.message)
        })
    }

    fun toast(msg: String?) {
        ToastUtils.show(msg ?: "")
    }

    fun toast(msg: Any?) {
        ToastUtils.show(msg.toString())
    }

    protected fun <VM : ViewModel> getViewModel(owner: ViewModelStoreOwner, clazz: Class<VM>): VM {
        val viewModel = ViewModelProvider(
            owner,
            ViewModelProvider.NewInstanceFactory()
        )[clazz]
        if(viewModel is BaseViewModel){
            viewModel.finish.observe(requireActivity(), Observer {
                if(it) requireActivity().finish()
            })
            viewModel.backPress.observe(requireActivity(), Observer {
                if(it) requireActivity().onBackPressed()
            })
            viewModel.toast.observe(requireActivity(), Observer {
                toast(it)
            })
        }
        return viewModel
    }

    protected fun<VM:ViewModel> getViewModel(clazz:Class<VM>) = getViewModel(requireActivity(),clazz)

    fun requestPermissions(permissions: Array<String>,callback:((result:Boolean,permissions:List<String>)->Unit)? = null){
        val activity = requireActivity()
        if(activity is BaseActivity){
            activity.requestPermissions(permissions,callback)
        }else{
            throw Exception("activity not is BaseActivity")
        }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        clearCoroutine()
    }
}