package com.flynnchow.zero.magician.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.model.StoreUpdateMessage
import com.flynnchow.zero.model.StoreModel
import com.flynnchow.zero.model.StoreVideo
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class StoreViewModel : BaseViewModel() {
    private val _store = MutableLiveData<StoreModel>()
    val store: LiveData<StoreModel> = _store
    private val _delete = MutableLiveData<Int>()
    val delete: LiveData<Int> = _delete
    private val _createVideo = MutableLiveData<Int>()
    val createVideo: LiveData<Int> = _createVideo
    private val _playerVideo = MutableLiveData<Int>()
    val playerVideo: LiveData<Int> = _playerVideo

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    fun initStore(id: Int) {
        startLaunch {
            val dao = RoomManager.instance.getStoreModelDao()
            val data = dao.getData(id)
            if (data.isNotEmpty()) {
                val storeData = data.first()
                _store.value = storeData
            } else {
                showToast("未找到故事")
                finish()
            }
        }
    }

    var name = "init"

    @Subscribe
    fun resetStore(msg: StoreUpdateMessage) {
        if (store.value?.id != null && store.value?.id == msg.message && msg.event == "update") {
            initStore(store.value?.id!!)
        } else if (msg.event == "delete") {
            _delete.value = (msg.message as? Int) ?: -1
        }
    }

    suspend fun updateStore(video: StoreVideo) {
        _store.value?.let {
            it.videoId = video.id!!
            val dao = RoomManager.instance.getStoreModelDao()
            dao.update(it)
            EventBus.getDefault().post(StoreUpdateMessage(it.id!!, "update"))
        }
    }

    suspend fun deleteStore() {
        _store.value?.let {
            val dao = RoomManager.instance.getStoreModelDao()
            dao.delete(it)
            EventBus.getDefault().post(StoreUpdateMessage(it.id!!, "delete"))
        }
    }

    fun videoClick() {
        store.value?.run {
            if (isCreateVideo()) {
                videoId.let {
                    _playerVideo.value = it
                }
            } else {
                id?.let {
                    _createVideo.value = it
                }
            }
        }
    }

}