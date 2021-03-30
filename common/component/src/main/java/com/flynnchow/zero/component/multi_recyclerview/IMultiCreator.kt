package com.flynnchow.zero.component.multi_recyclerview

import android.content.Context
import android.view.ViewGroup

interface IMultiCreator{
    fun create(context:Context,parent:ViewGroup): IMultiHolder<*>
}