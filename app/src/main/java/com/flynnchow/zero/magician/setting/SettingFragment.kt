package com.flynnchow.zero.magician.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.flynnchow.zero.baidu_sso.BaiduSsoActivity
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.component.dialog.LoadingDialog
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentSettingBinding
import com.flynnchow.zero.magician.main.MainActivity

class SettingFragment : MagicianFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    private val mViewModel by lazy {
        getViewModel(requireActivity(), SettingViewModel::class.java)
    }

    override fun onInitView() {

    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = mViewModel
        if (requireActivity() is MainActivity) {
            mBinding.activity = requireActivity() as MainActivity?
        }
        mViewModel.checkedBackup.observe(this, Observer {
            if (mViewModel.initializerFinish) {
                mViewModel.checkedChange(it)
            }
        })
        mViewModel.requestBaiduSso.observe(this, {
            BaiduSsoActivity.launch(requireContext(), activityLaunch)
        })
        mViewModel.loading.observe(this,{
            if(it){
                LoadingDialog.show(requireActivity(),"获取授权中")
            }else{
                LoadingDialog.dismiss()
            }
        })
    }

    override fun onActivityForResult(result: ActivityResult) {
        super.onActivityForResult(result)
        when (result.resultCode) {
            BaiduSsoActivity.RESULT_OK -> {
                mViewModel.onUpdateToken(BaiduSsoActivity.getToken(result))
            }
            BaiduSsoActivity.RESULT_FAIL -> {
                mViewModel.onUpdateToken(null)
            }
        }
    }
}