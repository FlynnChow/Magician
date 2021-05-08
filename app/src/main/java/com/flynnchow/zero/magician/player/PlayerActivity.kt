package com.flynnchow.zero.magician.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.base.util.FileUtils
import com.flynnchow.zero.common.activity.MagicianActivity
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityPlayerBinding
import com.flynnchow.zero.model.StoreVideo
import io.microshow.rxffmpeg.player.MeasureHelper
import io.microshow.rxffmpeg.player.RxFFmpegPlayerControllerImpl
import io.microshow.rxffmpeg.player.RxFFmpegPlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class PlayerActivity : MagicianActivity<ActivityPlayerBinding>(R.layout.activity_player) {
    companion object {
        fun onPlayerVideo(context: Context, videoId: Int) {
            context.startActivity(Intent(context, PlayerActivity::class.java).apply {
                putExtra("id", videoId)
            })
        }

        fun onPlayerVideo(context: Context, uri: String) {
            context.startActivity(Intent(context, PlayerActivity::class.java).apply {
                putExtra("uri", uri)
            })
        }

        fun onPlayerVideo(scope: CoroutineScope, context: Context, videoId: Int) {
            scope.launch {
                val dao = RoomManager.instance.getStoreVideoDao()
                val data = dao.getData(videoId)
                if (data.isNotEmpty()) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    val uri = FileProvider.getUriForFile(context, "", File(data.first().path))
                    intent.setDataAndType(uri, "video/*")
                    context.startActivity(intent)
                }
            }
        }
    }

    private val viewModel by lazy {
        getViewModel(PlayerViewModel::class.java)
    }

    override fun onInitView() {
        mBinding.back.setOnClickListener {
            onBackPressed()
        }
    }

    private var tempVideoPath = ""
    private val tempCache by lazy {
        getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath + File.separator + "cache" + File.separator + "player" + File.separator
    }

    override fun onInitObserver() {
        super.onInitObserver()
        viewModel.video.observe(this, Observer {
            initPlayer(it)
        })

        viewModel.uri.observe(this, Observer {
            initPlayer(it)
        })
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        val id = intent.getIntExtra("id", -1)
        val uri = intent.getStringExtra("uri") ?: ""
        if (id != -1) {
            viewModel.initVideo(id)
        } else if (uri.isNotEmpty()) {
            viewModel.initUri(uri)
        } else {
            showToast("视频无效")
            finish()
        }
    }

    private fun initPlayer(video: StoreVideo) {
        mBinding.player.switchPlayerCore(RxFFmpegPlayerView.PlayerCoreType.PCT_SYSTEM_MEDIA_PLAYER)
        mBinding.player.setController(
            RxFFmpegPlayerControllerImpl(this),
            MeasureHelper.FitModel.FM_FULL_SCREEN_HEIGHT
        )
        mBinding.player.setTextureViewEnabledTouch(false)
        mBinding.player.play(video.path, true)
    }

    private fun initPlayer(uri: String) {
        startLaunch {
            tempVideoPath = FileUtils.obtainPathFromUri(
                Uri.parse(uri),
                tempCache,
                System.currentTimeMillis().toString(),
                "mp4"
            ).toString()
            mBinding.player.switchPlayerCore(RxFFmpegPlayerView.PlayerCoreType.PCT_SYSTEM_MEDIA_PLAYER)
            mBinding.player.setController(
                RxFFmpegPlayerControllerImpl(this),
                MeasureHelper.FitModel.FM_FULL_SCREEN_HEIGHT
            )
            mBinding.player.setTextureViewEnabledTouch(false)
            mBinding.player.play(tempVideoPath, true)
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.player.resume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        FileUtils.deleteDirWithFile(tempVideoPath)
        mBinding.player.release()
    }
}