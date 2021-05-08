package com.flynnchow.zero.model

data class JpegExif(
    val orientation:Int,
    val dateTime:String,
    val make:String,
    val model:String,
    val imageLength:Int,
    val imageWidth:Int,
    val latitude:String,
    val longitude:String,
    val latitudeRef:String,
    val longitudeRef:String,
    val altitude:String
)
