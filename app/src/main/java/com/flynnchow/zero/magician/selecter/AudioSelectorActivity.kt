package com.flynnchow.zero.magician.selecter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.ActivityAudioSelectorBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.math.abs


class AudioSelectorActivity :
    MagicianActivity<ActivityAudioSelectorBinding>(R.layout.activity_audio_selector) {
    companion object {
        fun startPage(fragment: Fragment, master: Context, uri: Uri? = null) {
            fragment.startActivityForResult(
                Intent(
                    master,
                    AudioSelectorActivity::class.java
                ).apply {
                    putExtra("uri", uri?.toString())
                }, 0
            )
        }
    }

    private val adapter = AudioAdapter()
    private lateinit var mediaPlayer: MediaPlayer
    private var isSetAudio = false
    private var runTask = false
    private val viewModel by lazy {
        getViewModel(AudioSelectorViewModel::class.java)
    }

    override fun onInitView() {
        mBinding.listView.adapter = adapter
        mBinding.listView.addItemDecoration(AudioListDivider())
        mediaPlayer = MediaPlayer()
        (mBinding.listView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mBinding.back.setOnClickListener {
            onBackPressed()
        }
        mBinding.confirm.setOnClickListener {
            confirmAudio()
        }
        adapter.addSelectListener { audio, repeat ->
            viewModel.setAudio(audio)
            if (!repeat) {
                playAudio(audio.getUri())
            } else {
                changeState()
            }
        }
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        val uriString = intent.getStringExtra("uri")
        val uri: Uri? = if (uriString != null) Uri.parse(uriString) else null
        startLaunch(Dispatchers.Main) {
            val audioData = MediaProvider.instance.getAudioList()
            viewModel.initAudioList(audioData, uri)
        }
        setPlayerTask()
    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = viewModel
        viewModel.audioData.observe(this, Observer {
            adapter.setData(it)
        })
        viewModel.audioUpdate.observe(this, Observer {
            adapter.updateData(it)
        })
    }

    private fun playAudio(uri: Uri): Boolean {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this, uri)
        isSetAudio = true
        mediaPlayer.prepare()
        mediaPlayer.start()
        return true
    }

    private fun changeState(): Boolean {
        return if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            false
        } else {
            mediaPlayer.start()
            true
        }
    }

    private fun setPlayerTask() {
        runTask = true
        startLaunch {
            while (runTask) {
                delay(1000)
                if (mediaPlayer.isPlaying) {
                    val duration = mediaPlayer.duration
                    val position = mediaPlayer.currentPosition
                    adapter.setProgress(position, duration)
                } else if (abs(mediaPlayer.currentPosition - mediaPlayer.duration) < 1000 && mediaPlayer.duration > 0) {
                    adapter.setProgress(mediaPlayer.duration, mediaPlayer.duration)
                }
            }
        }
    }

    override fun onResume() {
        if (isSetAudio) {
            mediaPlayer.start()
        }
        super.onResume()
    }

    override fun onPause() {
        if (mediaPlayer.isPlaying && isSetAudio) {
            mediaPlayer.pause()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        super.onDestroy()
    }

    private fun confirmAudio() {
        val name = viewModel.selectName.value
        name?.let {
            val data = Intent().apply {
                putExtra("uri", viewModel.selectUri.value.toString())
                putExtra("name", it)
            }
            setResult(0, data)
            onBackPressed()
        }
    }
}