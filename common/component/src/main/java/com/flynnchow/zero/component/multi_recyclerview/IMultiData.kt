package com.flynnchow.zero.component.multi_recyclerview

abstract class IMultiData<Data>(var data:Data) {
    abstract fun getType():Int

    //用于判断数据是否相同
    open fun getCompared():String = hashCode().toString()
}