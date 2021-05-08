package com.flynnchow.zero.base.util

object StringUtils {
    fun isNotEmpty(str:String?):Boolean{
        return str != null && str.isNotEmpty()
    }

    fun isEmpty(str:String?):Boolean{
        return !isNotEmpty(str)
    }
}