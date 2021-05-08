package com.flynnchow.zero.magician.base.provider

import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.base.util.FileUtils
import com.flynnchow.zero.common.helper.BaiduPanHelper
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.work.BaiduUploadWorker
import com.flynnchow.zero.magician.base.work.ImageAsyncWorker
import com.flynnchow.zero.model.AudioModel
import com.flynnchow.zero.model.JpegExif
import com.flynnchow.zero.model.MediaModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.FileDescriptor
import java.io.IOException


class MediaProvider {
    companion object {
        @JvmStatic
        val instance: MediaProvider by lazy {
            MediaProvider()
        }

        fun getAlbumArt(album_id: Long): Bitmap? {
            var bm: Bitmap? = null
            try {
                val sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart")
                val uri = ContentUris.withAppendedId(sArtworkUri, album_id)
                val pfd: ParcelFileDescriptor? =
                    BaseApplication.instance.contentResolver.openFileDescriptor(uri, "r")
                if (pfd != null) {
                    val fd: FileDescriptor = pfd.fileDescriptor
                    bm = BitmapFactory.decodeFileDescriptor(fd)
                }
            } catch (e: Exception) {
            }
            return bm
        }
    }

    enum class MediaType {
        IMAGE,
        AUDIO,
        VIDEO
    }

    private var audioData: List<AudioModel>? = null

    private val _imageData = MutableLiveData<List<MediaModel>>()
    private val _videoData = MutableLiveData<List<MediaModel>>()
    private val _galleryData = MutableLiveData<List<MediaModel>>()

    private val _imageUpdate = MutableLiveData<MediaModel>()
    private val _videoUpdate = MutableLiveData<MediaModel>()
    private val _galleryUpdate = MutableLiveData<MediaModel>()

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

    suspend fun getAudioList(): List<AudioModel> {
        if (audioData == null) {
            audioData = readAudioStore()
        }
        return audioData ?: ArrayList()
    }

    fun doWork() {
        GlobalScope.launch {
            val getImagesJob = obtainGallery()
            getImagesJob.join()
            audioData = readAudioStore()
            doAsyncImageWork()
        }
    }

    suspend fun doAsyncImageWork() {
        updateExifInfo() //更新文件信息
        ImageAsyncWorker.instance.doLaunchImageAsync() //图像识别文件
//        BaiduUploadWorker.doAutoUploadWork()
    }

    private fun updateExifInfo() {
        GlobalScope.launch {
            val srcData = MediaProvider.instance.getImageList()
            for (data in srcData) {
                if (data.exifJson.isEmpty()) {
                    val inStream =
                        BaseApplication.instance.contentResolver.openInputStream(data.getUri())
                    inStream?.let {
                        try {
                            val exif = ExifInterface(it)
                            val exifBean = JpegExif(
                                exif.getAttributeInt(
                                    ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_NORMAL
                                ),
                                exif.getAttribute(
                                    ExifInterface.TAG_DATETIME,
                                ) ?: "",
                                exif.getAttribute(
                                    ExifInterface.TAG_MAKE,
                                ) ?: "",
                                exif.getAttribute(
                                    ExifInterface.TAG_MODEL,
                                ) ?: "",
                                exif.getAttributeInt(
                                    ExifInterface.TAG_IMAGE_LENGTH, 0
                                ),
                                exif.getAttributeInt(
                                    ExifInterface.TAG_IMAGE_WIDTH, 0
                                ),
                                exif.getAttribute(
                                    ExifInterface.TAG_GPS_LATITUDE,
                                ) ?: "",
                                exif.getAttribute(
                                    ExifInterface.TAG_GPS_LONGITUDE,
                                ) ?: "",
                                exif.getAttribute(
                                    ExifInterface.TAG_GPS_LATITUDE_REF,
                                ) ?: "",
                                exif.getAttribute(
                                    ExifInterface.TAG_GPS_LONGITUDE_REF,
                                ) ?: "",
                                exif.getAttribute(
                                    ExifInterface.TAG_GPS_ALTITUDE,
                                ) ?: "",
                            )
                            val newData = data.copy(exifJson = Gson().toJson(exifBean))
                            RoomManager.instance.getMediaDao().replaceInsert(newData)
                            withContext(Dispatchers.Main) {
                                updateMediaData(MediaType.IMAGE, newData)
                            }
                        } catch (e: IOException) {
                        } finally {
                            it.close()
                        }
                    }
                }
            }
        }
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
//        if (type == MediaType.IMAGE || type == MediaType.VIDEO) {
        if (type == MediaType.IMAGE) {
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
                val medias = readMetaStore(MediaType.IMAGE)
                imageDao.ignoreInsert(medias)
                checkImageUpdateState(medias)
                imageDao.getData()
            }
            val videoJob: Deferred<List<MediaModel>> = async(Dispatchers.IO) {
                readMetaStore(MediaType.VIDEO)
            }
            mergeToGallery(imageJob.await(), videoJob.await())
        }
    }

    private suspend fun checkImageUpdateState(medias: List<MediaModel>) {
        val imageDao = RoomManager.instance.getMediaDao()
        val imageMap = HashMap<String, MediaModel>()
        for (media in medias) {
            imageMap[media.id] = media
        }
        val deleteImages = ArrayList<MediaModel>()
        for (data in imageDao.getData()) {
            if (imageMap[data.id] == null) {
                deleteImages.add(data)
            }
        }
        imageDao.delete(deleteImages)
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
//            暂时不处理视频
//            val result = ArrayList<MediaModel>()
//            result.addAll(imageList)
//            result.addAll(videoList)
//            result.sortWith { o1, o2 -> (o2.addDate - o1.addDate).toInt() }
//            _galleryData.value = result
            _galleryData.value = _imageData.value
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

    private suspend fun readAudioStore(): List<AudioModel> {
        return withContext(Dispatchers.IO) {
            val result = ArrayList<AudioModel>()
            val uri = getUri(MediaType.AUDIO)
            val columns = getColumns(MediaType.AUDIO)
            val cursor = BaseApplication.instance.contentResolver.query(
                uri,
                null, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC"
            )
            cursor?.run {
                while (moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(columns[0]))
                    val title = cursor.getString(cursor.getColumnIndex(columns[1]))
                    val album = cursor.getString(cursor.getColumnIndex(columns[2]))
                    val artist = cursor.getString(cursor.getColumnIndex(columns[3]))
                    val fileName = cursor.getString(cursor.getColumnIndex(columns[4]))
                    val year = cursor.getString(cursor.getColumnIndex(columns[5]))
                    val duration = cursor.getInt(cursor.getColumnIndex(columns[6]))
                    val size = cursor.getLong(cursor.getColumnIndex(columns[7]))
                    val addDate = cursor.getLong(cursor.getColumnIndex(columns[8]))
                    val mimeType = cursor.getString(cursor.getColumnIndex(columns[9]))
                    val albumId = cursor.getLong(cursor.getColumnIndex(columns[10]))
                    result.add(
                        AudioModel(
                            id,
                            albumId,
                            addDate,
                            size,
                            year,
                            duration,
                            fileName,
                            mimeType,
                            title,
                            album,
                            artist
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
            MediaType.AUDIO -> {
                arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.YEAR,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.MIME_TYPE,
                    MediaStore.Audio.Media.ALBUM_ID,
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

    private fun getMediaType(type: MediaType): String {
        return when (type) {
            MediaType.VIDEO -> FileUtils.MediaType.video
            MediaType.AUDIO -> FileUtils.MediaType.audio
            MediaType.IMAGE -> FileUtils.MediaType.images
            else -> ""
        }
    }
}