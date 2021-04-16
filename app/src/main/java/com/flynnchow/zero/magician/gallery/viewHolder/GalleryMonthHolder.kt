package com.flynnchow.zero.magician.gallery.viewHolder

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynnchow.zero.component.multi_recyclerview.SimpleHolder
import com.flynnchow.zero.magician.databinding.ItemTitleScrollBinding
import com.flynnchow.zero.magician.gallery.GalleryType
import com.flynnchow.zero.magician.gallery.adapter.GalleryMonthAdapter
import com.flynnchow.zero.magician.gallery.viewdata.GalleryMonthData

class GalleryMonthHolder(mBinding: ItemTitleScrollBinding) :
    SimpleHolder<ItemTitleScrollBinding>(mBinding) {
    private val adapter = GalleryMonthAdapter(GalleryType.DEFAULT)
    private val layoutManager = LinearLayoutManager(mBinding.root.context)

    fun bindData(data: GalleryMonthData, type: Int) {
        mBinding.dateHint = data.getMonthHint()
        mBinding.showTitle = type == GalleryType.MONTH_FIVE
        mBinding.listView.layoutManager = layoutManager
        mBinding.listView.adapter = adapter
        (mBinding.listView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        adapter.onBind(type,data.data)
    }
}