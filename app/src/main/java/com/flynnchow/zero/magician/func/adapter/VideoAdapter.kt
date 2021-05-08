package com.flynnchow.zero.magician.func.adapter

import android.media.MediaMetadataRetriever
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemVideoBinding
import com.flynnchow.zero.magician.func.viewdata.VideoData
import com.flynnchow.zero.magician.player.PlayerActivity

class VideoAdapter : DataBindAdapter<ItemVideoBinding, VideoData>(R.layout.item_video) {
    override fun onBind(binding: ItemVideoBinding, viewData: VideoData, position: Int) {
        binding.root.setOnClickListener {
            PlayerActivity.onPlayerVideo(binding.root.context, viewData.data.getUri().toString())
        }
        binding.uri = viewData.data.getUri()
        binding.hint = "${viewData.duration/100L}:${(viewData.duration%100L).toString().padStart(2,'0')}"
    }

    fun setData(list:List<VideoData>){
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(media:VideoData){
        val itemIndex = itemCount
        data.add(media)
        notifyItemInserted(itemIndex)
    }
}