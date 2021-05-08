package com.flynnchow.zero.video_codec

object ZoomProvider {
    const val none = FFmpegHelper.FFmpegConfig.NONE
    const val zoom_add = 0
    const val zoom_sub = 1

    val zoomList = arrayListOf<Int>(
        none,
        zoom_add,
        zoom_sub,
    )

    val zoomName = mapOf<Int,String>(
        none to "无",
        zoom_add to "放大缩放",
        zoom_sub to "缩小缩放",
    )

    val zoomIcons = mapOf<Int,@androidx.annotation.RawRes Int>(
        none to R.drawable.none,
        zoom_add to R.drawable.zoom_add,
        zoom_sub to R.drawable.zoom_sub,
    )
}