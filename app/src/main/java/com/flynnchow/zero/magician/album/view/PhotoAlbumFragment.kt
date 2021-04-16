package com.flynnchow.zero.magician.album.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.flynnchow.zero.common.fragment.BindingFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.adapter.PhotoAlbumAdapter
import com.flynnchow.zero.magician.album.viewdata.PhotoAlbumViewModel
import com.flynnchow.zero.magician.base.helper.TitleFontHelper
import com.flynnchow.zero.magician.base.work.ImageAsyncWorker
import com.flynnchow.zero.magician.base.work.ImageAsyncWorker.ProgressStatus.*
import com.flynnchow.zero.magician.databinding.FragmentAlbumBinding
import com.flynnchow.zero.magician.search.view.SearchActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.Hold
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo
import com.hw.ycshareelement.transition.TextViewStateSaver


class PhotoAlbumFragment : BindingFragment<FragmentAlbumBinding>(R.layout.fragment_album) {
    private val pageAdapter by lazy { PhotoAlbumAdapter(requireActivity()) }
    private val mViewModel by lazy { getViewModel(PhotoAlbumViewModel::class.java) }

    private var isClosePrompt = false
    private var lastAsyncTime = 0L

    override fun onInitView() {
        TitleFontHelper.updateTitleFont(mBinding.title)
        mBinding.viewPager.adapter = pageAdapter
        mBinding.viewPager.offscreenPageLimit = 3
        TabLayoutMediator(
            mBinding.tabLayout,
            mBinding.viewPager
        ) { tab, position -> tab.text = pageAdapter.getTabText(position) }.attach()
        mBinding.editAlbum.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), AlbumManagerActivity::class.java), 0)
        }
        mBinding.search.setOnClickListener{
            onSearch()
        }
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {
        mViewModel.init()
    }

    override fun onInitObserver() {
        super.onInitObserver()
        ImageAsyncWorker.instance.progress.observe(this, {
            changeProgress(it)
        })
        mBinding.mlPromptClose.setOnClickListener {
            isClosePrompt = true
            hideMlPrompt()
        }
        mViewModel.tagData.observe(this, {
            pageAdapter.insertAlbum(it)
            checkEmptyState()
        })
    }

    private fun changeProgress(progress: ImageAsyncWorker.Progress) {
        when (progress.status) {
            RUNNING -> {
                if (lastAsyncTime != progress.date) {
                    lastAsyncTime = progress.date
                    isClosePrompt = false
                }
                showMlPrompt()
            }
            SUCCESS -> {
                hideMlPrompt()
            }
        }
        if (isClosePrompt)
            return
        val startHint = "正在识别：正在识别第"
        val count = progress.current.toString()
        val midHint = " 张图片，总数"
        val max = progress.max.toString()
        val spanBuilder = SpannableStringBuilder("${startHint}${count}${midHint}${max} ")
        spanBuilder.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple_200)),
            startHint.length,
            startHint.length + count.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        spanBuilder.setSpan(
            StyleSpan(Typeface.ITALIC),
            startHint.length,
            startHint.length + count.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        spanBuilder.setSpan(
            StyleSpan(Typeface.BOLD_ITALIC),
            startHint.length + count.length + midHint.length,
            startHint.length + count.length + midHint.length + max.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        mBinding.mlPromptText.text = spanBuilder
    }

    private fun showMlPrompt() {
        if (mBinding.mlPrompt.visibility != View.VISIBLE && !isClosePrompt) {
            mBinding.mlPrompt.visibility = View.VISIBLE
        }
    }

    private fun hideMlPrompt() {
        if (mBinding.mlPrompt.visibility != View.GONE) {
            mBinding.mlPrompt.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == 0) {
            startLaunch {
                val newData = mViewModel.getAlbums()
                val index = pageAdapter.updateAlbum(newData, mBinding.viewPager.currentItem)
                mBinding.viewPager.currentItem = index
                pageAdapter.notifyDataSetChanged()
                checkEmptyState()
            }
            pageAdapter.notifyDataSetChanged()
        }
    }

    private fun checkEmptyState(){
        if (pageAdapter.itemCount > 0){
            mBinding.empty.visibility = View.GONE
        }else{
            mBinding.empty.visibility = View.VISIBLE
        }
    }

    private fun onSearch(){
        val bundle =
            YcShareElement.buildOptionsBundle(requireActivity(), object : IShareElements {
                override fun getShareElements(): Array<ShareElementInfo<Parcelable>> {
                    return arrayOf(ShareElementInfo(mBinding.search))
                }
            })
        startActivity(Intent(requireContext(), SearchActivity::class.java),bundle)
    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }
}