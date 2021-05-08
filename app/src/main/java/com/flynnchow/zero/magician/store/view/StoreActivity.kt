package com.flynnchow.zero.magician.store.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityStoreBinding
import com.flynnchow.zero.magician.player.PlayerActivity
import com.flynnchow.zero.magician.store.adapter.StoreAdapter
import com.flynnchow.zero.magician.store.viewmodel.StoreViewModel
import com.flynnchow.zero.magician.video_create.view.VideoCreatorActivity
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo

class StoreActivity : MagicianActivity<ActivityStoreBinding>(R.layout.activity_store) {
    companion object {
        fun startStorePage(context: Activity,srcView:View, id: Int?) {
            val bundle =
                YcShareElement.buildOptionsBundle(context, object : IShareElements {
                    override fun getShareElements(): Array<ShareElementInfo<Parcelable>> {
                        return arrayOf(ShareElementInfo(srcView))
                    }
                })
            context.startActivity(Intent(context, StoreActivity::class.java).apply {
                putExtra("id", id)
            },bundle)
        }

        fun startStorePage(context: Context,id: Int?) {
            context.startActivity(Intent(context, StoreActivity::class.java).apply {
                putExtra("id", id)
            })
        }
    }
    private val viewModel by lazy {
        getViewModel(StoreViewModel::class.java)
    }
    private val adapter = StoreAdapter()

    override fun onInitView() {
        mBinding.menu.setOnClickListener {
            val popup = PopupMenu(this, mBinding.menu)
            popup.menuInflater.inflate(R.menu.store_config, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.store_add -> {

                    }
                    R.id.store_rename -> {

                    }
                    R.id.store_change -> {

                    }
                    R.id.store_edit -> {

                    }
                    R.id.store_delete -> {
                        startLaunch {
                            viewModel.deleteStore()
                            onBackPressed()
                        }
                    }
                }
                true
            }
            popup.setOnDismissListener {

            }
            popup.show()
        }
        mBinding.listView.adapter = adapter
        mBinding.back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = viewModel
        viewModel.store.observe(this){
            val imageList = it.getUriImageList()
            adapter.addData(imageList)
        }
        viewModel.createVideo.observe(this){
            VideoCreatorActivity.startCreatorPage(this,it)
        }
        viewModel.playerVideo.observe(this){
            PlayerActivity.onPlayerVideo(this,it)
        }
    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        YcShareElement.setEnterTransitions(this, object : IShareElements {
            override fun getShareElements(): Array<ShareElementInfo<Parcelable>> {
                return arrayOf(
                    ShareElementInfo(mBinding.storeImage)
                )
            }
        })
        YcShareElement.startTransition(this)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        val id = intent.getIntExtra("id", -1)
        if (id > 0){
            viewModel.initStore(id)
        }
    }
}