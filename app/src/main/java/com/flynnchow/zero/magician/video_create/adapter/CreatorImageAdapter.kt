package com.flynnchow.zero.magician.video_create.adapter

import android.net.Uri
import android.view.ViewGroup
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemCreatorImageBinding
import com.flynnchow.zero.magician.video_create.viewdata.CreatorImageData

class CreatorImageAdapter:DataBindAdapter<ItemCreatorImageBinding, CreatorImageData>(R.layout.item_creator_image) {
    private var imageClickListener:((lastPosition:Int,newPosition:Int)->Unit)? = null
    private var lastPosition = 0

    override fun onBind(binding: ItemCreatorImageBinding, viewData: CreatorImageData, position: Int) {
        binding.viewData = viewData
        binding.image.setOnClickListener {
            imageClickListener?.invoke(lastPosition,position)
            lastPosition = position
        }
        val metrics = binding.image.context.resources.displayMetrics
        val layoutParams:ViewGroup.MarginLayoutParams = binding.parentView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == 0){
            layoutParams.marginStart = metrics.widthPixels / 2
        }else{
            layoutParams.marginStart = 0
        }
        if (position == itemCount - 1){
            layoutParams.marginEnd = metrics.widthPixels / 2
        }else{
            layoutParams.marginEnd = 0
        }
    }

    fun addData(uri:Uri){
        val index = itemCount
        data.add(CreatorImageData(uri))
        notifyItemInserted(index)
    }

    fun addData(uris:List<Uri>){
        val index = itemCount
        data.addAll(uris.map {
            CreatorImageData(it)
        })
        notifyItemRangeInserted(index,uris.count())
    }

    fun addImageClickListener(func:((Int,Int)->Unit)){
        imageClickListener = func
    }
}