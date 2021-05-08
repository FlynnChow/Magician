package com.flynnchow.zero.model

import android.net.Uri
import androidx.exifinterface.media.ExifInterface
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
    @PrimaryKey val id: String,
    @ColumnInfo(name = "add_date") val addDate: Long,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "mimeType") val mimeType: String,
    @ColumnInfo(name = "mediaType") val mediaType: String,
    @ColumnInfo(name = "baidu_md5") var baiduMd5: String = "",
    @ColumnInfo(name = "baidu_fs_id") var baiduFsId: String = "",
    @ColumnInfo(name = "mlResultStr") var mlResultJson: String = "",
    @ColumnInfo(name = "exif") var exifJson: String = ""
) {
    @Ignore
    var mlResult: MlResult? = null
        get() {
            if (field != null || mlResultJson.isEmpty()) {
                return field
            }
            return jsonToMLResult()
        }

    @Ignore
    var exifResult: JpegExif? = null
        get() {
            if (field != null || mlResultJson.isEmpty()) {
                return field
            }
            return jsonToExif()
        }

    fun getUri(): Uri {
        return Uri.parse("content://media/external/${mediaType}/media/$id")
    }

    fun getCalendar(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = addDate * 1000
        }
    }

    fun getYear() = getCalendar().get(Calendar.YEAR)

    fun getMonth() = getCalendar().get(Calendar.MONTH) + 1

    fun getDay() = getCalendar().get(Calendar.DAY_OF_MONTH)

    fun clone() = MediaModel(
        id,
        addDate,
        size,
        width,
        height,
        name,
        mimeType,
        mediaType,
        baiduMd5,
        baiduFsId,
        mlResultJson,
        exifJson
    ).also {
        it.mlResult = mlResult
        it.exifResult = exifResult
    }

    private fun jsonToMLResult(): MlResult? {
        return try {
            mlResult = Gson().fromJson(mlResultJson, MlResult::class.java)
            mlResult
        } catch (e: Exception) {
            null
        }
    }

    private fun jsonToExif(): JpegExif? {
        return try {
            exifResult = Gson().fromJson(exifJson, JpegExif::class.java)
            exifResult
        } catch (e: Exception) {
            null
        }
    }

    fun getDegree(): Int {
        val exif = exifResult
        if (exif != null) {
            return when (exif.orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }
        return 0
    }
}