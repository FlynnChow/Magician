package com.flynnchow.zero.database

import androidx.room.Room
import com.flynnchow.zero.base.BaseApplication

class RoomManager {
    companion object{
        val instance:AppDataBase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(BaseApplication.instance,
                AppDataBase::class.java, "magician_db")
                .allowMainThreadQueries() //允许在主线程中查询
                .build()
        }
    }
}