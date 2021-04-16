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

@Entity(tableName = "media_data")
data class MediaModel(
    @PrimaryKey val id:String,
    @ColumnInfo(name = "add_date") val addDate:Long,
    @ColumnInfo(name = "size")val size:Long,
    @ColumnInfo(name = "width")val width:Int,
    @ColumnInfo(name = "height")val height:Int,
    @ColumnInfo(name = "name")val name:String,
    @ColumnInfo(name = "mimeType")val mimeType:String,
    @ColumnInfo(name = "mediaType") val mediaType:String,
    @ColumnInfo(name = "mlResultStr")var mlResultJson:String = ""
){
    @Ignore
    var mlResult:MlResult? = null
        get() {
            if (field != null||mlResultJson.isEmpty()){
                return field
            }
            return jsonToMLResult()
        }

    fun getUri():Uri{
        return Uri.parse("content://media/external/${mediaType}/media/$id")
    }

    fun getCalendar():Calendar{
        return Calendar.getInstance().apply {
            timeInMillis = addDate * 1000
        }
    }

    fun getYear() = getCalendar().get(Calendar.YEAR)

    fun getMonth() = getCalendar().get(Calendar.MONTH) + 1

    fun getDay() = getCalendar().get(Calendar.DAY_OF_MONTH)

    fun clone() = MediaModel(id,addDate,size,width,height,name,mimeType,mediaType,mlResultJson).also {
        it.mlResult = mlResult
    }

    private fun jsonToMLResult():MlResult?{
        return try {
            mlResult = Gson().fromJson(mlResultJson,MlResult::class.java)
            mlResult
        }catch (e:Exception){
            null
        }
    }
}