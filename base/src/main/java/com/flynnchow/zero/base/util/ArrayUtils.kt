package com.flynnchow.zero.base.util

object ArrayUtils {
    fun isNotEmpty(array : Array<*>?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : Array<*>?):Boolean{
        return !isNotEmpty(array)
    }

    fun isNotEmpty(array : IntArray?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : IntArray?):Boolean{
        return !isNotEmpty(array)
    }

    fun isNotEmpty(array : FloatArray?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : FloatArray?):Boolean{
        return !isNotEmpty(array)
    }

    fun isNotEmpty(array : DoubleArray?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : DoubleArray?):Boolean{
        return !isNotEmpty(array)
    }

    fun isNotEmpty(array : LongArray?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : LongArray?):Boolean{
        return !isNotEmpty(array)
    }

    fun isNotEmpty(array : ShortArray?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : ShortArray?):Boolean{
        return !isNotEmpty(array)
    }

    fun isNotEmpty(array : ByteArray?):Boolean{
        return array != null && array.isNotEmpty()
    }

    fun isEmpty(array : ByteArray?):Boolean{
        return !isNotEmpty(array)
    }
}