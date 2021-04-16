package com.flynnchow.zero.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_video")
data class StoreVideo(
    @PrimaryKey val id:String,
    val path:String,
    val duration:Long,
    val createDate:String,
)
