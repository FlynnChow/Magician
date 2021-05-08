package com.flynnchow.zero.model

data class PreCreateResponse(
    val errno:Int = 0,
    val path:String = "",
    val uploadid:String = "",
    val return_type:String = "",
    val block_list:List<Int>? = null
)

data class CreateResponse(
    val errno:Int = 0,
    val fs_id:String = "",
    val md5:String = "",
    val path:String = "",
    val size:Long = 0L
)

data class ImageResponse(
    val errno:Int = 0,
    val list:List<BaiduPanImage>? =  null
)

data class BaiduPanImage(
    val fs_id:String = "",
    val md5:String = "",
    val path:String = "",
    val size:Long = 0L,
    val server_filename:String,
    val thumbs:Map<String,String>? = null
)