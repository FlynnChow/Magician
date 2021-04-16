package com.flynnchow.zero.magician.album.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.album.viewdata.AlbumTitleData
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.ml_kit.MLKitHelper
import com.flynnchow.zero.model.MLPhotoAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumManagerViewModel:BaseViewModel() {
    private val _data = MutableLiveData<List<AlbumTitleData>>()
    val data:LiveData<List<AlbumTitleData>> = _data

    private val _newData = MutableLiveData<AlbumTitleData>()
    val newData:LiveData<AlbumTitleData> = _newData

    fun initAlbumTitle(list:List<AlbumTitleData>){
        startLaunch {
            val mediaData = MediaProvider.instance.getImageList()
            for (album in list){
                for (image in mediaData){
                    if (MLKitHelper.isHitLabel(image,album.album.key)){
                        album.uri = image.getUri()
                        break
                    }
                }
            }
            _data.postValue(list)
        }
    }

    suspend fun saveAlbum(albumList:List<AlbumTitleData>){
        val albums = albumList.map {
            it.album
        }
        for (index in albums.indices){
            albums[index].sort = index
        }
        val dao = RoomManager.instance.getMlAlbumDao()
        dao.replaceInsert(albums)
    }

    fun newCreateAlbum(keyWord:String){
        startLaunch {
            val query = RoomManager.instance.getMlAlbumDao().getData(keyWord)
            if (query.isNotEmpty()){
                showToast("分类相册已经存在")
                return@startLaunch
            }
            val newAlbum = AlbumTitleData(MLPhotoAlbum(keyWord))
            RoomManager.instance.getMlAlbumDao().ignoreInsert(newAlbum.album)
            withContext(Dispatchers.Default) {
                val mediaData = MediaProvider.instance.getImageList()
                for (image in mediaData){
                    if (MLKitHelper.isHitLabel(image,newAlbum.album.key)){
                        newAlbum.uri = image.getUri()
                        break
                    }
                }
            }
            _newData.value = newAlbum
        }
    }

    fun deleteAlbum(data:MLPhotoAlbum){
        startLaunch {
            RoomManager.instance.getMlAlbumDao().delete(data)
        }
    }

    suspend fun getUri(album: AlbumTitleData):Uri?{
        val mediaData = MediaProvider.instance.getImageList()
        for (image in mediaData){
            if (MLKitHelper.isHitLabel(image,album.album.key)){
                return image.getUri()
            }
        }
        return null
    }
}