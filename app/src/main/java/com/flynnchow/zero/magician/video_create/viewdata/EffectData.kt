package com.flynnchow.zero.magician.video_create.viewdata

import androidx.annotation.RawRes

class EffectData(val id:Int,val title:String,@RawRes val img:Int?) {
    var onCheckListener:((id:Int)->Unit)? = null
}