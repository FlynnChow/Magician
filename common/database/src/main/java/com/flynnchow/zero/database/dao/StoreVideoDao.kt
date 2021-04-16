package com.flynnchow.zero.database.dao

import androidx.room.*
import com.flynnchow.zero.model.AppConfig
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreVideo

@Dao
interface StoreVideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(args:List<StoreVideo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(arg:StoreVideo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(args:List<StoreVideo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(arg:StoreVideo)

    @Update
    suspend fun update(args:List<StoreVideo>)

    @Update
    suspend fun update(arg:StoreVideo)

    @Delete
    suspend fun delete(args:List<StoreVideo>)

    @Delete
    suspend fun delete(arg:StoreVideo)

    @Query("DELETE FROM store_video")
    suspend fun clear()

    @Query("SELECT * FROM store_video")
    suspend fun getData():List<StoreVideo>
}