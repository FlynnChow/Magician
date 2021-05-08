package com.flynnchow.zero.video_codec

object EffectProvider {
    const val none = FFmpegHelper.FFmpegConfig.NONE
    const val effect_0 = 0
    const val effect_1 = 1
    const val effect_2 = 2
    const val effect_3 = 3

    val effectList = arrayListOf<Int>(
        none,
        effect_0,
        effect_1,
        effect_2,
        effect_3
    )

    val effectName = mapOf<Int, String>(
        none to "无",
        effect_0 to "背景拉伸",
        effect_1 to "背景模糊",
        effect_2 to "水平镜像",
        effect_3 to "垂直镜像"
    )

    val effectIcons = mapOf<Int, @androidx.annotation.RawRes Int>(
        none to R.drawable.none,
        effect_0 to R.drawable.effect,
        effect_1 to R.drawable.effect,
        effect_2 to R.drawable.hflip,
        effect_3 to R.drawable.vflip
    )
}