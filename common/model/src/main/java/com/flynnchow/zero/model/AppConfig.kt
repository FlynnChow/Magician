package com.flynnchow.zero.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flynnchow.zero.base.BaseApplication

@Entity(tableName = "app_config")
data class AppConfig(
    @PrimaryKey val packageName: String = BaseApplication.instance.packageName,
    @ColumnInfo(name = "init_albums") var initAlbums: Boolean = false,
    @ColumnInfo(name = "baidu_token") var baiduToken:String = "",
    @ColumnInfo(name = "use_auto_backup") var useAutoBackup:Boolean = false
)