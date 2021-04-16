package com.flynnchow.zero.magician.gallery.viewHolder

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.component.multi_recyclerview.SimpleHolder
import com.flynnchow.zero.magician.databinding.ItemTitleScrollBinding
import com.flynnchow.zero.magician.gallery.GalleryType
import com.flynnchow.zero.magician.gallery.adapter.GalleryImageAdapter
import com.flynnchow.zero.magician.gallery.viewdata.GalleryDayData

class GalleryDayHolder(mBinding: ItemTitleScrollBinding, private val manager:RecyclerView.LayoutManager) :
    SimpleHolder<ItemTitleScrollBinding>(mBinding) {
    private val adapter = GalleryImageAdapter(GalleryType.DEFAULT)

    fun bindData(data: GalleryDayData, type: Int) {
        mBinding.dateHint = data.getDayHint()
        mBinding.showTitle = type == GalleryType.DAY_ONE||type == GalleryType.DAY_THREE
        mBinding.listView.layoutManager = manager
        mBinding.listView.adapter = adapter
        (mBinding.listView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        adapter.onBind(type,data.images)
    }
}