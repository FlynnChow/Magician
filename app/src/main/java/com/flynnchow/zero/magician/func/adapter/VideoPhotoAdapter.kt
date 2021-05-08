package com.flynnchow.zero.magician.func.adapter

import android.media.MediaMetadataRetriever
import android.net.Uri
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemVideoBinding
import com.flynnchow.zero.magician.func.viewdata.VideoPhotoData
import com.flynnchow.zero.magician.player.PlayerActivity
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreVideo

class VideoPhotoAdapter : DataBindAdapter<ItemVideoBinding, VideoPhotoData>(R.layout.item_video) {
    override fun onBind(binding: ItemVideoBinding, viewData: VideoPhotoData, position: Int) {
        binding.root.setOnClickListener {
            PlayerActivity.onPlayerVideo(binding.root.context, viewData.data.id ?: 0)
        }
        binding.uri = viewData.data.path
        binding.hint = "${viewData.duration / 100L}:${(viewData.duration % 100L).toString().padStart(2, '0')}"
    }

    fun setData(list: List<VideoPhotoData>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(media: VideoPhotoData) {
        val itemIndex = itemCount
        data.add(media)
        notifyItemInserted(itemIndex)
    }
}