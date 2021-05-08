package com.flynnchow.zero.magician.video_create.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.util.StringUtils
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.component.dialog.LoadingDialog
import com.flynnchow.zero.component.dialog.ProgressDialog
import com.flynnchow.zero.component.view.dp
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityVideoCreatorBinding
import com.flynnchow.zero.magician.player.PlayerActivity
import com.flynnchow.zero.magician.store.viewmodel.StoreViewModel
import com.flynnchow.zero.magician.video_create.adapter.CreatorImageAdapter
import com.flynnchow.zero.magician.video_create.viewmodel.VideoConfigViewModel
import com.flynnchow.zero.magician.video_create.viewmodel.VideoCreatorViewModel
import com.flynnchow.zero.video_codec.FFmpeg
import com.flynnchow.zero.video_codec.FFmpegHelper
import com.google.android.material.tabs.TabLayout

class VideoCreatorActivity :
    MagicianActivity<ActivityVideoCreatorBinding>(R.layout.activity_video_creator) {
    companion object {
        fun startCreatorPage(context: Context, id: Int?) {
            context.startActivity(Intent(context, VideoCreatorActivity::class.java).apply {
                putExtra("id", id)
            })
        }
    }
    private val storeViewModel by lazy {
        getViewModel(StoreViewModel::class.java)
    }
    private val creatorViewModel by lazy {
        getViewModel(VideoCreatorViewModel::class.java)
    }
    private val configViewModel by lazy {
        getViewModel(VideoConfigViewModel::class.java)
    }
    private val imageAdapter by lazy {
        CreatorImageAdapter()
    }
    private var scrollOffsetX = 0
    private var progressDialog: ProgressDialog? = null


    private var lastFragment: Fragment? = null
    private lateinit var fragments: List<Fragment>

    override fun onInitView() {
        initFragment()
        mBinding.imageListView.adapter = imageAdapter
        mBinding.imageListView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.imageListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollOffsetX += dx
                val offset = scrollOffsetX
                val currentPosition = (offset / 54.dp)
                creatorViewModel.setPosition(currentPosition)
            }
        })
        mBinding.back.setOnClickListener {
            onBackPressed()
        }
        mBinding.save.setOnClickListener {
            creatorViewModel.onCreate(FFmpegHelper.build(this, configViewModel.getConfig().apply {
                uris = storeViewModel.store.value?.getUriImageList()
            }))
        }
        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text?.apply {
                    when (this) {
                        "转场" -> {
                            switchFragment(fragments[0])
                        }
                        "特效" -> {
                            switchFragment(fragments[1])
                        }
                        "音乐" -> {
                            switchFragment(fragments[2])
                        }
                        "文字" -> {
                            switchFragment(fragments[3])
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        imageAdapter.addImageClickListener { lastPosition, newPosition ->
            val offset = mBinding.imageListView.computeHorizontalScrollOffset()
            val newOffset = newPosition * 54.dp
            val range = newOffset - offset
            mBinding.imageListView.scrollBy(range, 0)
        }
    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = creatorViewModel
        storeViewModel.store.observe(this) {
            val imageList = it.getUriImageList()
            imageAdapter.addData(imageList)
            creatorViewModel.setUrisData(imageList)
            configViewModel.setTitle(it.title)
        }
        creatorViewModel.video.observe(this, Observer {
            startLaunch {
                storeViewModel.updateStore(it)
                if (it.id != null) {
                    PlayerActivity.onPlayerVideo(this, it.id!!)
                    finish()
                }
            }
        })
        creatorViewModel.progress.observe(this, Observer {
            if (it) {
                progressDialog?.dismiss()
                progressDialog = ProgressDialog(this, "正在制作影集中", "切到后台执行")
                progressDialog?.isConfirmClickable(false)
                progressDialog?.addCancelListener {
                    creatorViewModel.onCancelCodecTask()
                }
                progressDialog?.show()
            } else {
                progressDialog?.dismiss()
            }
        })

        creatorViewModel.loading.observe(this, Observer {
            if (it) {
                LoadingDialog.show(this,"正在取消任务中…")
            } else {
                LoadingDialog.dismiss()
            }
        })

        FFmpeg.ffProgress.observe(this, {
            it?.let {
                if (StringUtils.isNotEmpty(it.message)) {
                    progressDialog?.setMessage("${it.message}…（${it.currentTask}/${it.totalTask}）")
                } else {
                    progressDialog?.setMessage("")
                }
                progressDialog?.setProgress(it.progress)
                progressDialog?.setProgressVisibility(!it.hideProgress)
            }
        })
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        val id = intent.getIntExtra("id", -1)
        if (id > 0) {
            storeViewModel.initStore(id)
        }
    }

    private fun initFragment() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(VideoTransitionFragment())
        fragmentList.add(VideoEffectFragment())
        fragmentList.add(VideoAudioFragment())
        fragmentList.add(VideoTextFragment())
        fragments = fragmentList
        switchFragment(fragments[0])
    }

    private fun switchFragment(fragment: Fragment) {
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
}