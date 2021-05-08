package com.flynnchow.zero.magician.func.adapter

import android.media.MediaMetadataRetriever
import android.net.Uri
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ItemImageBaiduBinding
import com.flynnchow.zero.magician.databinding.ItemVideoBinding
import com.flynnchow.zero.magician.func.viewdata.VideoPhotoData
import com.flynnchow.zero.magician.player.PlayerActivity
import com.flynnchow.zero.model.BaiduPanImage
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreVideo

class BaiduPanAdapter :
    DataBindAdapter<ItemImageBaiduBinding, BaiduPanImage>(R.layout.item_image_baidu) {
    override fun onBind(binding: ItemImageBaiduBinding, viewData: BaiduPanImage, position: Int) {
        binding.url = viewData.thumbs?.get("url3")
        binding.root.setOnClickListener {
            ImageHelper.viewImage(
                binding.image,
                data.map {
                    viewData.thumbs?.get("url3")
                }.toTypedArray(),
                position
            )
        }
    }

    fun setData(list: List<BaiduPanImage>) {
        data.addAll(list)
        notifyDataSetChanged()
    }
}