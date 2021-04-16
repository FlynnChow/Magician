package com.flynnchow.zero.magician.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.viewdata.AlbumTitleData
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ItemAlbumTitleBinding
import com.flynnchow.zero.magician.databinding.ItemImageMediumBinding
import com.flynnchow.zero.magician.databinding.ItemSearchImageBinding
import com.flynnchow.zero.magician.databinding.ItemTitleScrollBinding
import com.flynnchow.zero.magician.gallery.viewHolder.GalleryMonthHolder
import com.flynnchow.zero.magician.gallery.viewdata.GalleryDayData
import com.flynnchow.zero.magician.gallery.viewdata.GalleryMonthData
import com.flynnchow.zero.magician.search.viewdata.SearchMonthData
import com.flynnchow.zero.model.MediaModel

class SearchImageAdapter() :
    DataBindAdapter<ItemSearchImageBinding, MediaModel>(R.layout.item_search_image) {
    private var srcData: List<SearchMonthData>? = null
    override fun onBind(binding: ItemSearchImageBinding, viewData: MediaModel, position: Int) {
        binding.viewData = viewData
        binding.root.setOnClickListener {
            if (srcData != null) {
                var point = 0
                var index = 0
                val srcImages = ArrayList<MediaModel>()
                for (searchData in srcData!!) {
                    for (image in searchData.images) {
                        if (image.id == viewData.id) {
                            point = index
                        }
                        srcImages.add(image)
                        index++
                    }
                }
                ImageHelper.viewImage(binding.image, srcImages.toTypedArray(), point)
            } else {
                ImageHelper.viewImage(binding.image, viewData)
            }
        }
    }

    fun onBind(bindData: List<MediaModel>, srcData: List<SearchMonthData>) {
        data.clear()
        data.addAll(bindData)
        this.srcData = srcData
    }
}