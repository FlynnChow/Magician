package com.flynnchow.zero.magician.store.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.PopupMenu
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.dialog.EditDialog
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.helper.TitleFontHelper
import com.flynnchow.zero.magician.databinding.FragmentStoreBinding
import com.flynnchow.zero.magician.store.adapter.StoreListAdapter
import com.flynnchow.zero.magician.store.viewmodel.StoreListViewModel
import com.flynnchow.zero.magician.store.viewmodel.StoreViewModel
import com.google.android.material.transition.platform.Hold
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.luck.picture.lib.PictureSelector

class StoreFragment : MagicianFragment<FragmentStoreBinding>(R.layout.fragment_store) {
    companion object {
        const val REQUEST_CREATE_STORE = 0
    }

    private val viewModel by lazy {
        getViewModel(StoreListViewModel::class.java)
    }
    private val storeViewModel by lazy {
        getViewModel(StoreViewModel::class.java)
    }

    private val adapter by lazy {
        StoreListAdapter(requireActivity())
    }

    override fun onInitView() {
        mBinding.menu.setOnClickListener {
            val popup = PopupMenu(requireContext(), mBinding.menu)
            popup.menuInflater.inflate(R.menu.store_list_config, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.store_list_edit -> {

                    }
                    R.id.store_list_create -> {
                        createNewStoreToTitle()
                    }
                }
                true
            }
            popup.setOnDismissListener {

            }
            popup.show()
        }
        mBinding.listView.adapter = adapter
        TitleFontHelper.updateTitleFont(mBinding.title)
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {
        viewModel.initData()
    }

    override fun onInitObserver() {
        super.onInitObserver()
        viewModel.newStore.observe(this, {
            adapter.addFirstData(it,mBinding.listView)
            checkEmptyState()
        })
        viewModel.stores.observe(this, {
            adapter.addData(it)
            checkEmptyState()
        })
        storeViewModel.delete.observe(this, {
            LogDebug("测试","update")
            adapter.removeData(it)
            checkEmptyState()
        })
        storeViewModel.name = "Frg"
    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    private fun createNewStoreToTitle() {
        startLaunch {
            EditDialog(requireActivity(), "创建故事", "请输入您的故事名", "选择照片")
                .addConfirmListener {
                    viewModel.createToTitle(it)
                    createNewStoreToPhoto()
                }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CREATE_STORE) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            if (selectList.isNotEmpty()) {
                viewModel.createToImages(selectList.map {
                    it.path
                })
                startLaunch {
                    val newId = viewModel.createNewStore()
                    newId?.run {
                        StoreActivity.startStorePage(requireContext(), this)
                    }
                }
            }
        }
    }

    private fun createNewStoreToPhoto() {
        ImageHelper.selectPicture(this, REQUEST_CREATE_STORE)
    }

    private fun checkEmptyState(){
        if (adapter.itemCount == 0){
            mBinding.emptyView.showEmptyView("还没有故事，赶快创建一个吧！")
        }else{
            mBinding.emptyView.hideEmptyView()
        }
    }
}