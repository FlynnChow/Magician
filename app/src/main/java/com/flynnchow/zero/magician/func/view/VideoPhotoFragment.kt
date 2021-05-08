package com.flynnchow.zero.magician.func.view

import android.media.MediaMetadataRetriever
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.func.adapter.VideoAdapter
import com.flynnchow.zero.magician.func.adapter.VideoPhotoAdapter
import com.flynnchow.zero.magician.func.base.FuncFragment
import com.flynnchow.zero.magician.func.viewdata.VideoData
import com.flynnchow.zero.magician.func.viewdata.VideoPhotoData
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoPhotoFragment: FuncFragment() {
    private val adapter = VideoPhotoAdapter()

    override fun onInitView() {
        funcViewModel.setTitle("影集")

        mBinding.listView.layoutManager = GridLayoutManager(requireContext(),3)
        mBinding.listView.adapter = adapter
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {
        startLaunch {
            val videoList = RoomManager.instance.getStoreVideoDao().getData()
            withContext(Dispatchers.Main){
                adapter.setData(mapVideoData(videoList))
                funcViewModel.setHint("${adapter.itemCount}个影集")
            }
        }
    }

    private suspend fun mapVideoData(data:List<StoreVideo>):List<VideoPhotoData>{
        return withContext(Dispatchers.Default){
            val mapData = data.map {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(it.path)
                val duration =
                    ((retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))?.toLong()
                        ?: 0) / 1000L
                VideoPhotoData(it,duration)
            }
            withContext(Dispatchers.Main){
                mapData
            }
        }
    }

}