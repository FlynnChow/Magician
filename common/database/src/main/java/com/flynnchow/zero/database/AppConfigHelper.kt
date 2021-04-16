package com.flynnchow.zero.database

import com.flynnchow.zero.model.AppConfig

object AppConfigHelper {
    suspend fun getAppConfig():AppConfig{
        val configs = RoomManager.instance.getAppConfigDao().getData()
        val config:AppConfig
        if (configs.isEmpty()){
            config = AppConfig()
            RoomManager.instance.getAppConfigDao().replaceInsert(config)
        }else{
            config = configs.first()
        }
        return config
    }

    suspend fun updateAppConfig(config:AppConfig){
        RoomManager.instance.getAppConfigDao().replaceInsert(config)
    }
}