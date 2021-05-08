package com.flynnchow.zero.magician.selecter

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.model.AudioModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioSelectorViewModel : BaseViewModel() {
    private val _audioData = MutableLiveData<List<MusicData>>()
    val audioData: LiveData<List<MusicData>> = _audioData
    private val _audioUpdate = MutableLiveData<MusicData>()
    val audioUpdate: LiveData<MusicData> = _audioUpdate
    private val _selectUri = MutableLiveData<Uri?>()
    val selectUri:LiveData<Uri?> = _selectUri
    private val _selectName = MutableLiveData<String?>()
    val selectName:LiveData<String?> = _selectName

    fun initAudioList(audioList: List<AudioModel>,uri: Uri?) {
        val defaultBitMap =
            BitmapFactory.decodeResource(BaseApplication.instance.resources, R.drawable.music)
        _selectUri.value = uri
        _audioData.value = audioList.map {
            MusicData(it, defaultBitMap)
        }
        startLaunch(Dispatchers.Default) {
            for (audio in audioList) {
                val album = MediaProvider.getAlbumArt(audio.albumId)
                album?.run {
                    withContext(Dispatchers.Main) {
                        _audioUpdate.value = MusicData(audio,album)
                    }
                }
            }
        }
    }

    fun setAudio(audio: AudioModel?){
        _selectUri.value = audio?.getUri()
        _selectName.value = audio?.audioName
    }
}