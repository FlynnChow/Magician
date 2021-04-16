package com.flynnchow.zero.magician.base.provider

import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.util.FileUtils
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.base.work.ImageAsyncWorker
import com.flynnchow.zero.model.MediaModel
import kotlinx.coroutines.*

class MediaProvider {
    companion object {
        @JvmStatic
        val instance: MediaProvider by lazy {
            MediaProvider()
        }
    }

    enum class MediaType {
        IMAGE,
        AUDIO,
        VIDEO
    }

    private val _audioData = MutableLiveData<List<MediaModel>>()

    private val _imageData = MutableLiveData<List<MediaModel>>()
    private val _videoData = MutableLiveData<List<MediaModel>>()
    private val _galleryData = MutableLiveData<List<MediaModel>>()

    private val _imageUpdate = MutableLiveData<MediaModel>()
    private val _videoUpdate = MutableLiveData<MediaModel>()
    private val _galleryUpdate = MutableLiveData<MediaModel>()

    val audioData: LiveData<List<MediaModel>> = _audioData

    val imageData: LiveData<List<MediaModel>> = _imageData
    val videoData: LiveData<List<MediaModel>> = _videoData
    val galleryData: LiveData<List<MediaModel>> = _galleryData

    val imageUpdate: LiveData<MediaModel> = _imageUpdate
    val videoUpdate: LiveData<MediaModel> = _videoUpdate
    val galleryUpdate: LiveData<MediaModel> = _galleryUpdate

    fun getImageList(): ArrayList<MediaModel> {
        return (_imageData.value as? ArrayList) ?: ArrayList()
    }

    fun getGalleryList(): ArrayList<MediaModel> {
        return (_galleryData.value as? ArrayList) ?: ArrayList()
    }

    fun getVideoList(): ArrayList<MediaModel> {
        return (_videoData.value as? ArrayList) ?: ArrayList()
    }

    fun getAudioList(): ArrayList<MediaModel> {
        return (_audioData.value as? ArrayList) ?: ArrayList()
    }

    fun doWork() {
        GlobalScope.launch {
            val getImagesJob = obtainGallery()
            getImagesJob.join()
            doAsyncImage()
        }
    }

    suspend fun doAsyncImage(){
        ImageAsyncWorker.instance.doLaunchImageAsync()
    }

    fun updateMediaData(type: MediaType, data: MediaModel) {
        if (type == MediaType.VIDEO) {
            val needUpdate = updateMediaModelViewData(_videoData, data)
            if (needUpdate) {
                _videoUpdate.value = data
            }
        }
        if (type == MediaType.IMAGE) {
            val needUpdate = updateMediaModelViewData(_imageData, data)
            if (needUpdate) {
                _imageUpdate.value = data
            }
        }
        if (type == MediaType.IMAGE || type == MediaType.VIDEO) {
            val needUpdate = updateMediaModelViewData(_galleryData, data)
            if (needUpdate) {
                _galleryUpdate.value = data
            }
        }
    }

    private fun updateMediaModelViewData(
        liveData: MutableLiveData<List<MediaModel>>,
        data: MediaModel
    ): Boolean {
        val list = ArrayList(liveData.value)
        (list as? ArrayList)?.run {
            var isFind = false
            for (index in this.indices) {
                val model = this[index]
                if (data.id == model.id) {
                    isFind = true
                    if (data != model) {
                        this[index] = data
                        liveData.value = this
                        return true
                    }
                }
            }
            if (!isFind) {
                add(data)
                sortWith { o1, o2 -> (o2.addDate - o1.addDate).toInt() }
                liveData.value = this
                return true
            }
        }
        return false
    }

    private fun obtainGallery(): Job {
        return GlobalScope.launch {
            val imageJob: Deferred<List<MediaModel>> = async(Dispatchers.IO) {
                val imageDao = RoomManager.instance.getMediaDao()
                imageDao.ignoreInsert(readMetaStore(MediaType.IMAGE))
                imageDao.getData()
            }
            val videoJob: Deferred<List<MediaModel>> = async(Dispatchers.IO) {
                readMetaStore(MediaType.VIDEO)
            }
            mergeToGallery(imageJob.await(), videoJob.await())
        }
    }

    private suspend fun mergeToGallery(
        imageList: List<MediaModel>,
        videoList: List<MediaModel>
    ) {
        withContext(Dispatchers.Main) {
            if (_imageData.value == null) {
                _imageData.value = imageList
            }
            if (_videoData.value == null) {
                _videoData.value = videoList
            }
            val result = ArrayList<MediaModel>()
            result.addAll(imageList)
            result.addAll(videoList)
            result.sortWith { o1, o2 -> (o2.addDate - o1.addDate).toInt() }
            _galleryData.value = result
        }
    }

    private suspend fun readMetaStore(type: MediaType): List<MediaModel> {
        return withContext(Dispatchers.IO) {
            val result = ArrayList<MediaModel>()
            val uri = getUri(type)
            val columns = getColumns(type)
            val cursor = BaseApplication.instance.contentResolver.query(
                uri,
                null, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC"
            )
            cursor?.run {
                while (moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(columns[0]))
                    val size = cursor.getLong(cursor.getColumnIndex(columns[1]))
                    val name = cursor.getString(cursor.getColumnIndex(columns[2]))
                    val addDate = cursor.getLong(cursor.getColumnIndex(columns[3]))
                    val width = cursor.getInt(cursor.getColumnIndex(columns[4]))
                    val height = cursor.getInt(cursor.getColumnIndex(columns[5]))
                    val mimeType = cursor.getString(cursor.getColumnIndex(columns[6]))
                    result.add(
                        MediaModel(
                            id,
                            addDate,
                            size,
                            width,
                            height,
                            name,
                            mimeType,
                            getMediaType(type)
                        )
                    )
                }
            }
            cursor?.close()
            result
        }
    }

    private fun getColumns(type: MediaType): Array<String> {
        return when (type) {
            MediaType.VIDEO -> {
                arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.WIDTH,
                    MediaStore.Video.Media.HEIGHT,
                    MediaStore.Video.Media.MIME_TYPE,
                )
            }
            MediaType.AUDIO -> {
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.WIDTH,
                    MediaStore.Audio.Media.HEIGHT,
                    MediaStore.Audio.Media.MIME_TYPE,
                )
            }
            MediaType.IMAGE -> {
                arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.MIME_TYPE,
                )
            }
        }
    }

    private fun getUri(type: MediaType): Uri {
        return when (type) {
            MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private fun getMediaData(type: MediaType): List<MediaModel>? {
        return when (type) {
            MediaType.VIDEO -> _videoData.value
            MediaType.AUDIO -> _audioData.value
            MediaType.IMAGE -> _imageData.value
        }
    }

    private suspend fun setMediaData(type: MediaType, result: List<MediaModel>) {
        withContext(Dispatchers.Main) {
            when (type) {
                MediaType.VIDEO -> _videoData.value = result
                MediaType.AUDIO -> _audioData.value = result
                MediaType.IMAGE -> _imageData.value = result
            }
        }
    }

    private fun getMediaType(type: MediaType): String {
        return when (type) {
            MediaType.VIDEO -> FileUtils.MediaType.video
            MediaType.AUDIO -> FileUtils.MediaType.audio
            MediaType.IMAGE -> FileUtils.MediaType.images
            else -> ""
        }
    }
}