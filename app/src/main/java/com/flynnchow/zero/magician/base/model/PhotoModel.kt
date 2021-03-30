package com.flynnchow.zero.magician.base.model

import android.content.Intent
import android.net.Uri

data class PhotoModel(
    val id:String,
    val addDate:Long,
    val modifiedDate:Long,
    val size:Long,
    val width:Int,
    val height:Int,
    val data:String,
    val name:String
){
    fun getUri():Uri{
        return Uri.parse(data)
    }
}