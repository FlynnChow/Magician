package com.flynnchow.zero.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ml_photo_album")
data class MLPhotoAlbum(
    @PrimaryKey val key: String,
    var name:String = key,
    var sort:Int = 0,
    @ColumnInfo(name = "create_date") val createDate:Long = System.currentTimeMillis()
)