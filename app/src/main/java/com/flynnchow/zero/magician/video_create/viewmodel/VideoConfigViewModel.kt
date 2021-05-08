package com.flynnchow.zero.magician.video_create.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.model.AudioModel
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreModel
import com.flynnchow.zero.video_codec.FFmpegHelper
import com.flynnchow.zero.video_codec.TransitionProvider

class VideoConfigViewModel : BaseViewModel() {
    var mSpeed = 0
    var mVolume = 0
    var mTransition = FFmpegHelper.FFmpegConfig.NONE
    var mEffects = FFmpegHelper.FFmpegConfig.NONE
    var mZoom = FFmpegHelper.FFmpegConfig.NONE
    var mPan = FFmpegHelper.FFmpegConfig.NONE
    var mAudio: Uri? = null
    var titleText = MutableLiveData<String>()
    var audioName = MutableLiveData("请选择")

    fun setVideoSpeed(speed: Int) {
        mSpeed = speed
    }

    fun setVolume(progress: Int) {
        mVolume = progress
    }

    fun setTransition(transition: Int) {
        mTransition = transition
    }

    fun setZoom(zoom: Int) {
        mZoom = zoom
    }

    fun setPan(pan: Int) {
        mPan = pan
    }

    fun setEffect(effect: Int) {
        mEffects = effect
    }

    fun setAudio(audio:Uri,name:String) {
        mAudio = audio
        audioName.value = name
    }

    fun setTitle(arg: String) {
        titleText.value = arg
    }

    fun getConfig(): FFmpegHelper.FFmpegConfig {
        val config = FFmpegHelper.FFmpegConfig()
        config.transition = mTransition
        config.zoom = mZoom
        config.pan = mPan
        config.effect = mEffects
        config.speed = mSpeed
        config.volume = mVolume
        config.audio = mAudio
        config.title = audioName.value ?: ""
        return config
    }
}