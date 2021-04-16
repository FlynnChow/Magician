package com.flynnchow.zero.magician.album.viewdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.AppConfigHelper
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.model.MLPhotoAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoAlbumViewModel : BaseViewModel() {
    companion object{
        private val defaultAlbums = arrayOf(
            "植物",
            "动物",
            "人物"
        )
    }
    private val _tagData = MutableLiveData<List<MLPhotoAlbum>>()
    val tagData: LiveData<List<MLPhotoAlbum>> = _tagData

    fun init() {
        startLaunch {
            val data = getAlbums()
            withContext(Dispatchers.Main){
                _tagData.value = data
            }
        }
    }

    suspend fun getAlbums():List<MLPhotoAlbum>{
        val dao = RoomManager.instance.getMlAlbumDao()
        var data = dao.getData()
        if (data.isEmpty()){
            val config = AppConfigHelper.getAppConfig()
            if (!config.initAlbums){
                AppConfigHelper.updateAppConfig(config.apply {
                    initAlbums = true
                })
                val newData = defaultAlbums.map {
                    MLPhotoAlbum(it)
                }.toList()
                dao.replaceInsert(newData)
                data = dao.getData()
            }
        }
        return data
    }
}