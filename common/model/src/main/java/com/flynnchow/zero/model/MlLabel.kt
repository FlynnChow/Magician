package com.flynnchow.zero.model

data class MlLabel(
    val index:Int,
    val label:String,
    val confidence:Float,
    val mlType:String
)
