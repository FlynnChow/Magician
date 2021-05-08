package com.flynnchow.zero.model

data class StoreUpdateMessage (
    val message: Any,
    val event:String
)

data class BackupExpiredMessage (
    val message: Any
)