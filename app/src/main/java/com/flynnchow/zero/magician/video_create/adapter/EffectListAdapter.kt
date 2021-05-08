package com.flynnchow.zero.magician.video_create.adapter

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemCreatorImageBinding
import com.flynnchow.zero.magician.databinding.ItemEffectBinding
import com.flynnchow.zero.magician.databinding.ItemStoreImageBinding
import com.flynnchow.zero.magician.store.viewdata.StoreImageData
import com.flynnchow.zero.magician.video_create.viewdata.CreatorImageData
import com.flynnchow.zero.magician.video_create.viewdata.EffectData

class EffectListAdapter:DataBindAdapter<ItemEffectBinding, EffectData>(R.layout.item_effect) {
    private var imageClickListener:((lastPosition:Int,newPosition:Int)->Unit)? = null
    private var lastPosition:Int = 0

    fun addData(srcData:EffectData){
        val index = itemCount
        data.add(srcData)
        notifyItemInserted(index)
    }

    fun addData(srcData:List<EffectData>){
        val index = itemCount
        data.addAll(srcData)
        notifyItemRangeInserted(index,srcData.count())
    }

    fun addImageClickListener(func:((Int,Int)->Unit)){
        imageClickListener = func
    }

    override fun onBind(binding: ItemEffectBinding, viewData: EffectData, position: Int) {
        binding.viewData = viewData
        binding.selected = position == lastPosition
        binding.root.setOnClickListener {
            val oldPosition = lastPosition
            lastPosition = position
            if (oldPosition != lastPosition){
                viewData.onCheckListener?.invoke(viewData.id)
                notifyItemChanged(oldPosition)
                notifyItemChanged(lastPosition)
            }
        }
    }
}