package com.flynnchow.zero.database.dao

import androidx.room.*
import com.flynnchow.zero.model.MLPhotoAlbum
import com.flynnchow.zero.model.MediaModel

@Dao
interface MLAlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(args:List<MLPhotoAlbum>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(arg:MLPhotoAlbum)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(args:List<MLPhotoAlbum>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(arg:MLPhotoAlbum)

    @Update
    suspend fun update(args:List<MLPhotoAlbum>)

    @Update
    suspend fun update(arg:MLPhotoAlbum)

    @Delete
    suspend fun delete(args:List<MLPhotoAlbum>)

    @Delete
    suspend fun delete(arg:MLPhotoAlbum)

    @Query("DELETE FROM ml_photo_album")
    suspend fun clear()

    @Query("SELECT * FROM ml_photo_album ORDER BY create_date ASC")
    suspend fun getData():List<MLPhotoAlbum>

    @Query("SELECT * FROM ml_photo_album Where `key` == :keyword")
    suspend fun getData(keyword:String):List<MLPhotoAlbum>
}