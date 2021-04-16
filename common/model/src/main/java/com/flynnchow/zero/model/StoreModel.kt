package com.flynnchow.zero.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_model")
data class StoreModel(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "images_json") val imagesJson: String,
    @ColumnInfo(name = "video_id") val videoId: Int = NOT_EXIT,
    @ColumnInfo(name = "create_date") val createDate: Long = System.currentTimeMillis()
) {
    companion object {
        const val NOT_EXIT = -1
    }
}
