package com.flynnchow.zero.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flynnchow.zero.database.dao.*
import com.flynnchow.zero.model.AppConfig
import com.flynnchow.zero.model.MLPhotoAlbum
import com.flynnchow.zero.model.MediaModel

@Database(entities = [MediaModel::class,MLPhotoAlbum::class,AppConfig::class], version = 1)
abstract class AppDataBase: RoomDatabase()  {
    abstract fun getMediaDao(): MediaDao

    abstract fun getMlAlbumDao(): MLAlbumDao

    abstract fun getAppConfigDao(): AppConfigDao

    abstract fun getStoreModelDao(): StoreModelDao

    abstract fun getStoreVideoDao(): StoreVideoDao
}