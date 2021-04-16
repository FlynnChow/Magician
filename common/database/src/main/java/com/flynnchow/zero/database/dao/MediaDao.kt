package com.flynnchow.zero.database.dao

import androidx.room.*
import com.flynnchow.zero.model.MediaModel

@Dao
interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(args:List<MediaModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(arg:MediaModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(args:List<MediaModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(arg:MediaModel)

    @Update
    suspend fun update(args:List<MediaModel>)

    @Update
    suspend fun update(arg:MediaModel)

    @Delete
    suspend fun delete(args:List<MediaModel>)

    @Delete
    suspend fun delete(arg:MediaModel)

    @Query("DELETE FROM media_data")
    suspend fun clear()

    @Query("SELECT * FROM media_data ORDER BY add_date DESC")
    suspend fun getData():List<MediaModel>
}