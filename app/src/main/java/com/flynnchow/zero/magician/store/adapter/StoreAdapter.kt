package com.flynnchow.zero.magician.store.adapter

import android.net.Uri
import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemStoreImageBinding
import com.flynnchow.zero.magician.store.viewdata.StoreImageData

class StoreAdapter:DataBindAdapter<ItemStoreImageBinding, StoreImageData>(R.layout.item_store_image) {
    override fun onBind(binding: ItemStoreImageBinding, viewData: StoreImageData, position: Int) {
        binding.viewData = viewData
        binding.image.setOnClickListener {
            ImageHelper.viewImage(binding.image,data.map {
                it.uri
            }.toTypedArray(),position)
        }
    }

    fun addData(uri:Uri){
        val index = itemCount
        data.add(StoreImageData(uri))
        notifyItemInserted(index)
    }

    fun addData(uris:List<Uri>){
        val index = itemCount
        data.addAll(uris.map {
            StoreImageData(it)
        })
        notifyItemRangeInserted(index,uris.count())
    }
}