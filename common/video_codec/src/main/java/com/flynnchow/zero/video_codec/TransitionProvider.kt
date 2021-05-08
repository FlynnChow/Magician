package com.flynnchow.zero.video_codec

object TransitionProvider {
    const val none = FFmpegHelper.FFmpegConfig.NONE
    const val fade = 0

    val transientList = arrayListOf<Int>(
        none,
        fade
    )

    val transientName = mapOf<Int,String>(
        none to "无",
        fade to "淡入淡出",
    )

    val transientIcons = mapOf<Int,@androidx.annotation.RawRes Int>(
        none to R.drawable.none,
        fade to R.drawable.fade,
    )
}