package com.flynnchow.zero.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_video")
data class StoreVideo(
    val path:String,
    val duration:Int,
    @PrimaryKey val id:Int? = null,
    val createDate:Long = System.currentTimeMillis(),
)
