package com.flynnchow.zero.common.util

object ListUtils {
    fun isNotEmpty(list: List<*>?):Boolean{
        return list != null && list.isNotEmpty()
    }

    fun isEmpty(list: List<*>?):Boolean{
        return !isNotEmpty(list)
    }
}