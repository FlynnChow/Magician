package com.flynnchow.zero.magician.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreVideo

class PlayerViewModel : BaseViewModel() {
    private var _video = MutableLiveData<StoreVideo>()
    var video: LiveData<StoreVideo> = _video

    private var _uri = MutableLiveData<String>()
    var uri: LiveData<String> = _uri

    fun initVideo(id:Int){
        startLaunch {
            val dao = RoomManager.instance.getStoreVideoDao()
            val data = dao.getData(id)
            if (data.isNotEmpty()){
                _video.value = data.first()
            }else{
                showToast("未找到视频")
                finish()
            }
        }
    }

    fun initUri(uri:String){
        _uri.value = uri
    }

}