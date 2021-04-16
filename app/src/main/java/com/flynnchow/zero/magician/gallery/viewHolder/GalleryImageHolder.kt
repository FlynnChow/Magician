package com.flynnchow.zero.magician.gallery.viewHolder

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.multi_recyclerview.SimpleHolder
import com.flynnchow.zero.component.view.dp
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ItemImageMediumBinding
import com.flynnchow.zero.magician.databinding.ItemImageMiniBinding
import com.flynnchow.zero.magician.databinding.ItemImageOriginBinding
import com.flynnchow.zero.magician.gallery.viewdata.GalleryImageData
import com.flynnchow.zero.model.MediaModel
import com.stfalcon.imageviewer.StfalconImageViewer

class GalleryImageHolder(mBinding: ViewDataBinding) :
    SimpleHolder<ViewDataBinding>(mBinding) {
    private var viewData: GalleryImageData? = null

    fun bindData(data: MediaModel) {
        if (viewData == null) viewData = GalleryImageData(data)
        when (mBinding) {
            is ItemImageOriginBinding -> {
                val widthPadding = 16.dp
                val metrics = mBinding.image.context.resources.displayMetrics
                val width = metrics.widthPixels - widthPadding
                val layoutParams = mBinding.image.layoutParams
                layoutParams.height = width * data.height / data.width
                layoutParams.width = width
                mBinding.image.layoutParams = layoutParams
                mBinding.viewData = viewData
                mBinding.root.setOnClickListener {
                    ImageHelper.viewImage(mBinding.image, MediaProvider.instance.getGalleryList().toTypedArray(),findIndex(data.id))
                }
            }
            is ItemImageMediumBinding -> {
                mBinding.viewData = viewData
                mBinding.root.setOnClickListener {
                    ImageHelper.viewImage(mBinding.image, MediaProvider.instance.getGalleryList().toTypedArray(),findIndex(data.id))
                }
            }
            is ItemImageMiniBinding -> {
                mBinding.viewData = viewData
                mBinding.root.setOnClickListener {
                    ImageHelper.viewImage(mBinding.image, MediaProvider.instance.getGalleryList().toTypedArray(),findIndex(data.id))
                }
            }
        }
    }

    private fun findIndex(id: String): Int {
        val list = MediaProvider.instance.getGalleryList()
        for ((index, data) in list.withIndex()) {
            if (data.id == id)
                return index
        }
        return 0
    }
}