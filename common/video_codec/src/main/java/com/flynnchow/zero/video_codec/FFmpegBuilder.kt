package com.flynnchow.zero.video_codec

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.flynnchow.zero.base.helper.LogDebug
import java.io.File

object FFmpegHelper {
    fun build(context: Context,config: FFmpegConfig):FFmpeg{
        return FFmpeg(context,config)
    }

    class FFmpegConfig{
        companion object{
            const val NONE = -1
        }
        var transition = FFmpegHelper.FFmpegConfig.NONE
        var zoom = FFmpegHelper.FFmpegConfig.NONE
        var pan = FFmpegHelper.FFmpegConfig.NONE
        var effect = FFmpegHelper.FFmpegConfig.NONE
        var uris:List<Uri>? = null
        var audio:Uri? = null
        var volume:Int = 100
        var speed:Int = 2
        var title:String = ""
    }
}