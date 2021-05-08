package com.flynnchow.zero.database.dao

import androidx.room.*
import com.flynnchow.zero.model.AppConfig
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.StoreModel

@Dao
interface StoreModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(args:List<StoreModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceInsert(arg:StoreModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(args:List<StoreModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsert(arg:StoreModel)

    @Update
    suspend fun update(args:List<StoreModel>)

    @Update
    suspend fun update(arg:StoreModel)

    @Delete
    suspend fun delete(args:List<StoreModel>)

    @Delete
    suspend fun delete(arg:StoreModel)

    @Query("DELETE FROM store_model")
    suspend fun clear()

    @Query("SELECT * FROM store_model ORDER BY create_date DESC")
    suspend fun getData():List<StoreModel>


    @Query("SELECT * FROM store_model WHERE id = :id ORDER BY create_date DESC")
    suspend fun getData(id:Int):List<StoreModel>
}