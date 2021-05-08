package com.flynnchow.zero.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "store_model")
data class StoreModel(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "images_json") val imagesJson: String,
    @ColumnInfo(name = "image_path") val image: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "video_id") var videoId: Int = NOT_EXIT,
    @ColumnInfo(name = "create_date") val createDate: Long = System.currentTimeMillis()
){
    @Ignore
    private var imageList: List<String>? = null

    companion object {
        const val NOT_EXIT = -1
    }

    fun getDateTime(): String =
        SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(Date(createDate))

    fun getImageCount(): String {
        initJsonData()
        return (imageList?.size ?: 0).toString()
    }

    fun isCreateVideo(): Boolean {
        return videoId != NOT_EXIT
    }

    private fun initJsonData() {
        if (imageList == null) {
            val jsonList = Gson().fromJson<List<String>>(
                imagesJson,
                object : TypeToken<List<String>>() {}.type
            )
            imageList = jsonList ?: ArrayList()
        }
    }

    fun getImageList():List<String>{
        initJsonData()
        return imageList?:ArrayList()
    }

    fun getUriImageList():List<Uri>{
        val imageList =  getImageList()
        return imageList.map {
            Uri.parse(it)
        }
    }
}
