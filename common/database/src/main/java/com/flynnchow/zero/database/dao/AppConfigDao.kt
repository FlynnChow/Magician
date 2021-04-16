package com.flynnchow.zero.database.dao

import androidx.room.*
import com.flynnchow.zero.model.AppConfig
import com.flynnchow.zero.model.MediaModel

@Dao
interface AppConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(args:List<AppConfig>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(arg:AppConfig)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(args:List<AppConfig>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(arg:AppConfig)

    @Update
    suspend fun update(args:List<AppConfig>)

    @Update
    suspend fun update(arg:AppConfig)

    @Delete
    suspend fun delete(args:List<AppConfig>)

    @Delete
    suspend fun delete(arg:AppConfig)

    @Query("DELETE FROM app_config")
    suspend fun clear()

    @Query("SELECT * FROM app_config")
    suspend fun getData():List<AppConfig>
}