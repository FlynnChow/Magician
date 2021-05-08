package com.flynnchow.zero.video_codec

object PanProvider {
    const val none = FFmpegHelper.FFmpegConfig.NONE
    const val translation_x_add = 0
    const val translation_x_sub = 1
    const val translation_y_add = 2
    const val translation_y_sub = 3
    const val translation_x_add_y_add = 4
    const val translation_x_add_y_sub = 5
    const val translation_x_sub_y_add = 6
    const val translation_x_sub_y_sub = 7

    val panList = arrayListOf<Int>(
        none,
        translation_x_add,
        translation_x_sub,
        translation_y_add,
        translation_y_sub,
        translation_x_add_y_add,
        translation_x_add_y_sub,
        translation_x_sub_y_add,
        translation_x_sub_y_sub
    )

    val panName = mapOf<Int,String>(
        none to "无",
        translation_x_add to "右平移",
        translation_x_sub to "左平移",
        translation_y_add to "上平移",
        translation_y_sub to "下平移",
        translation_x_add_y_add to "右下平移",
        translation_x_add_y_sub to "右上平移",
        translation_x_sub_y_add to "左下平移",
        translation_x_sub_y_sub to "左上平移",
    )

    val panIcons = mapOf<Int,@androidx.annotation.RawRes Int>(
        none to R.drawable.none,
        translation_x_add to R.drawable.right,
        translation_x_sub to R.drawable.left,
        translation_y_add to R.drawable.top,
        translation_y_sub to R.drawable.bottom,
        translation_x_add_y_add to R.drawable.translation_merge,
        translation_x_add_y_sub to R.drawable.translation_merge,
        translation_x_sub_y_add to R.drawable.translation_merge,
        translation_x_sub_y_sub to R.drawable.translation_merge,
    )
}