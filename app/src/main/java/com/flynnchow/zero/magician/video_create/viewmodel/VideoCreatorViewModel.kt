package com.flynnchow.zero.magician.video_create.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.model.StoreModel
import com.flynnchow.zero.model.StoreVideo
import com.flynnchow.zero.video_codec.CancelEvent
import com.flynnchow.zero.video_codec.FFmpeg
import com.flynnchow.zero.video_codec.FFmpegHelper
import kotlinx.coroutines.delay

class VideoCreatorViewModel : BaseViewModel() {
    private val uris: ArrayList<Uri> = ArrayList()
    private var ffmpeg:FFmpeg? = null
    private var _position = MutableLiveData<Int>()
    var position: LiveData<Int> = _position
    private var _progress = MutableLiveData<Boolean>()
    var progress: LiveData<Boolean> = _progress
    private var _loading = MutableLiveData<Boolean>()
    var loading: LiveData<Boolean> = _loading
    private var _video = MutableLiveData<StoreVideo>()
    var video: LiveData<StoreVideo> = _video

    fun setUrisData(uris: List<Uri>) {
        this.uris.clear()
        this.uris.addAll(uris)
    }

    fun setPosition(newPosition: Int) {
        if (uris.size == 0) {
            return
        }
        var next = newPosition
        if (next < 0) {
            next = 0
        }
        if (next >= uris.size) {
            next = uris.size - 1
        }
        if (_position.value != next) {
            _position.value = next
        }
    }

    fun getCurrentUri(position: Int): Uri? {
        if (uris.size > 0) {
            return uris[position]
        }
        return null
    }

    fun onCancelCodecTask(){
        if (ffmpeg?.isWorking() == true){
            if (_progress.value == true){
                _progress.value = false
            }
            _loading.value = true
            ffmpeg?.cancel()
        }
    }

    fun onCreate(ffmpeg: FFmpeg) {
        this.ffmpeg = ffmpeg
        startLaunch({
            _progress.value = true
            val video = ffmpeg.run()
            val dao = RoomManager.instance.getStoreVideoDao()
            dao.replaceInsert(video)
            _video.value = dao.getData().first()
        }, {
            if(it !is CancelEvent){
                showToast(it.message ?: "")
            }
            delay(10000)
        }, {
            this.ffmpeg = null
            if (_progress.value == true){
                _progress.value = false
            }
            if (_loading.value == true){
                _loading.value = false
                showToast("成功取消制作影集")
            }
        })
    }
}