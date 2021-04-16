package com.flynnchow.zero.magician.album.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.component.dialog.DeleteDialog
import com.flynnchow.zero.component.dialog.EditDialog
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.*
import com.flynnchow.zero.magician.album.adapter.AlbumTitleAdapter
import com.flynnchow.zero.magician.album.viewdata.AlbumTitleData
import com.flynnchow.zero.magician.album.viewdata.PhotoAlbumViewModel
import com.flynnchow.zero.magician.album.viewmodel.AlbumManagerViewModel
import com.flynnchow.zero.magician.databinding.ActivityAlbumManagerBinding

class AlbumManagerActivity :
    BindingActivity<ActivityAlbumManagerBinding>(R.layout.activity_album_manager) {
    private val titleAdapter = AlbumTitleAdapter(this)
    private val titleTouchHelper = ItemTouchHelper(AlbumTitleTouchHelper(titleAdapter))
    private var lastFragment: Fragment? = null
    private val albumViewModel by lazy {
        getViewModel(PhotoAlbumViewModel::class.java)
    }
    private val albumManagerViewModel by lazy {
        getViewModel(AlbumManagerViewModel::class.java)
    }
    private var currentData: AlbumTitleData? = null

    override fun onInitView() {
        mBinding.activity = this
        mBinding.titleListView.adapter = titleAdapter
        titleTouchHelper.attachToRecyclerView(mBinding.titleListView)
        mBinding.back.setOnClickListener {
            onBackPressed()
        }
        (mBinding.titleListView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        albumViewModel.init()
        titleAdapter.addCheckListener {
            mBinding.title = it.album.key
            mBinding.date = "创建时间: ${it.album.createDate}"
            currentData = it
            switchFragment(it.fragment)
        }
    }

    private fun switchFragment(fragment: ClassificationFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            transaction.add(mBinding.container.id, fragment)
        }
        if (lastFragment != null) {
            transaction.hide(lastFragment!!)
        }
        lastFragment = fragment
        transaction.show(fragment).commitAllowingStateLoss()
    }

    override fun onInitObserver() {
        super.onInitObserver()
        albumManagerViewModel.data.observe(this, {
            titleAdapter.notifyDataSetChanged()
            checkEmptyState()
        })
        albumManagerViewModel.newData.observe(this, {
            titleAdapter.insertAlbum(it)
            it.position = titleAdapter.itemCount - 1
            val oldPosition = titleAdapter.checkPosition
            titleAdapter.checkPosition = it.position
            if (oldPosition > 0) {
                titleAdapter.notifyItemChanged(oldPosition)
            }
            titleAdapter.notifyItemChanged(titleAdapter.checkPosition)
            mBinding.titleListView.scrollToPosition(titleAdapter.itemCount)
            if (titleAdapter.itemCount > 1) {
                switchFragment(it.fragment)
            }
            checkEmptyState()
        })
        albumViewModel.tagData.observe(this, {
            titleAdapter.insertAlbum(it)
            checkEmptyState()
            albumManagerViewModel.initAlbumTitle(titleAdapter.data)
        })
    }

    override fun onBackPressed() {
        startLaunch {
            onSaveData()
            super.onBackPressed()
        }
    }

    private suspend fun onSaveData() {
        albumManagerViewModel.saveAlbum(titleAdapter.data)
        setResult(0)
    }

    fun addAlbum(view: View) {
        startLaunch {
            EditDialog(this, "新建分类相册", "请输入关键字", "新建")
                .addConfirmListener {
                    albumManagerViewModel.newCreateAlbum(it)
                }.show()
        }
    }

    fun removeAlbum(view: View) {
        if (titleAdapter.itemCount == 0){
            showToast("还未创建相册")
            return
        }
        DeleteDialog(this, "是否确定删除分类相册？").addConfirmListener {
            currentData?.run {
                titleAdapter.removeAlbum(this)
                albumManagerViewModel.deleteAlbum(album)
                checkEmptyState()
            }
        }.show()
    }

    private fun checkEmptyState() {
        if (titleAdapter.itemCount > 0) {
            mBinding.name
            mBinding.empty.visibility = View.GONE
            mBinding.content.visibility = View.VISIBLE
        } else {
            mBinding.empty.visibility = View.VISIBLE
            mBinding.content.visibility = View.GONE
            mBinding.title = "未选择相册"
            mBinding.date = ""
        }
    }
}