package com.flynnchow.zero.common.util

object StringUtils {
    fun isNotEmpty(str:String?):Boolean{
        return str != null && str.isNotEmpty()
    }

    fun isEmpty(str:String?):Boolean{
        return !isNotEmpty(str)
    }
}