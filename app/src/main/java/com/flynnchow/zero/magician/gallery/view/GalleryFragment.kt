package com.flynnchow.zero.magician.gallery.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.fragment.BindingFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.helper.TitleFontHelper
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.base.work.ImageAsyncWorker
import com.flynnchow.zero.magician.databinding.FragmentGalleryBinding
import com.flynnchow.zero.magician.gallery.GalleryType
import com.flynnchow.zero.magician.gallery.adapter.GalleryMainAdapter
import com.flynnchow.zero.magician.gallery.viewmodel.GalleryViewModel
import com.google.android.material.transition.platform.Hold
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough


class GalleryFragment : BindingFragment<FragmentGalleryBinding>(R.layout.fragment_gallery) {
    private val galleryViewModel by lazy {
        getViewModel(GalleryViewModel::class.java)
    }
    private val adapter by lazy {
        GalleryMainAdapter(GalleryType.DEFAULT)
    }

    override fun onInitView() {
        mBinding.galleyRecyclerView.adapter = adapter
        mBinding.galleyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.gallerySort.setOnClickListener {
            when (adapter.type) {
                GalleryType.DAY_ONE -> {
                    mBinding.gallerySort.setImageResource(R.drawable.layout_3)
                    adapter.type = GalleryType.DAY_THREE
                }
                GalleryType.DAY_THREE -> {
                    mBinding.gallerySort.setImageResource(R.drawable.layout_1)
                    adapter.type = GalleryType.MONTH_FIVE
                }
                GalleryType.MONTH_FIVE -> {
                    mBinding.gallerySort.setImageResource(R.drawable.layout_2)
                    adapter.type = GalleryType.DAY_ONE
                }
            }
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        }
        TitleFontHelper.updateTitleFont(mBinding.title)
    }

    override fun onInitObserver() {
        super.onInitObserver()
        MediaProvider.instance.galleryData.observe(this, {
            galleryViewModel.initGallery(it)
        })
        galleryViewModel.data.observe(this, {
            adapter.setData(it)
        })
        MediaProvider.instance.galleryUpdate.observe(this, {
            adapter.updateData(it, mBinding.galleyRecyclerView)
        })
        (mBinding.galleyRecyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }
}