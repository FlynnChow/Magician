package com.flynnchow.zero.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.util.StringUtil
import com.google.gson.Gson
import java.lang.Exception
import java.util.*

data class AudioModel(
    val id:Int,
    val albumId:Long,
    val addDate:Long,
    val size:Long,
    val year:String?,
    val duration:Int,
    val fileName:String,
    val mimeType:String,
    val audioName:String,
    val albumName:String,
    val artist:String?
){
    private fun getCalendar():Calendar{
        return Calendar.getInstance().apply {
            timeInMillis = addDate * 1000
        }
    }

    fun getUri():Uri{
        return Uri.parse("content://media/external/audio/media/$id")
    }

    fun getYear() = getCalendar().get(Calendar.YEAR)

    fun getMonth() = getCalendar().get(Calendar.MONTH) + 1

    fun getDay() = getCalendar().get(Calendar.DAY_OF_MONTH)
}