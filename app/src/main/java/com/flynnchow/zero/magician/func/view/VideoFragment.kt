package com.flynnchow.zero.magician.func.view

import android.media.MediaMetadataRetriever
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.flynnchow.zero.base.util.ListUtils
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.func.adapter.VideoAdapter
import com.flynnchow.zero.magician.func.base.FuncFragment
import com.flynnchow.zero.magician.func.viewdata.VideoData
import com.flynnchow.zero.model.MediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoFragment: FuncFragment() {
    private val adapter = VideoAdapter()

    override fun onInitView() {
        funcViewModel.setTitle("视频")

        mBinding.listView.layoutManager = GridLayoutManager(requireContext(),3)
        mBinding.listView.adapter = adapter
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }

    override fun onInitObserver() {
        super.onInitObserver()
        val data = MediaProvider.instance.getVideoList()
        if (ListUtils.isNotEmpty(data)){
            startLaunch {
                adapter.setData(mapVideoData(data))
                funcViewModel.setHint("${adapter.itemCount}个视频")
            }
        }else{
            MediaProvider.instance.videoData.observe(this,{
                if (it.isNotEmpty()){
                    MediaProvider.instance.videoData.removeObservers(this)
                    startLaunch {
                        adapter.setData(mapVideoData(it))
                        funcViewModel.setHint("${adapter.itemCount}个视频")
                    }
                }
            })
        }
        MediaProvider.instance.videoUpdate.observe(this,{
            startLaunch {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(requireContext(),it.getUri())
                val duration =
                    ((retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))?.toLong()
                        ?: 0) / 1000L
                adapter.addData(VideoData(it,duration))
                funcViewModel.setHint("${adapter.itemCount}个视频")
            }
        })
    }

    private suspend fun mapVideoData(data:List<MediaModel>):List<VideoData>{
        return withContext(Dispatchers.Default){
            val mapData = data.map {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(requireContext(),it.getUri())
                val duration =
                    ((retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))?.toLong()
                        ?: 0) / 1000L
                VideoData(it,duration)
            }
            withContext(Dispatchers.Main){
                mapData
            }
        }
    }
}