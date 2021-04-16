package com.flynnchow.zero.magician.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.viewdata.AlbumTitleData
import com.flynnchow.zero.magician.databinding.ItemAlbumTitleBinding
import com.flynnchow.zero.magician.databinding.ItemImageMediumBinding
import com.flynnchow.zero.magician.databinding.ItemTitleScrollBinding
import com.flynnchow.zero.magician.gallery.viewHolder.GalleryMonthHolder
import com.flynnchow.zero.magician.gallery.viewdata.GalleryDayData
import com.flynnchow.zero.magician.gallery.viewdata.GalleryMonthData
import com.flynnchow.zero.magician.search.viewdata.SearchMonthData
import com.flynnchow.zero.model.MediaModel

class SearchAdapter() : DataBindAdapter<ItemTitleScrollBinding, SearchMonthData>(R.layout.item_title_scroll) {
    fun setData(images: List<SearchMonthData>) {
        data.clear()
        data.addAll(images)
        notifyDataSetChanged()
    }

    fun updateData(image: MediaModel,recyclerView: RecyclerView) {
        val monthHint = GalleryDayData.getMonthHint(image)
        for (index in 0 until itemCount) {
            val monthData = data[index]
            if (monthData.getMonthHint() == monthHint) {
                for (imageIndex in monthData.images.indices) {
                    val monthImage = monthData.images[imageIndex]
                    if (monthImage.id == image.id) {
                        (monthData.images as? ArrayList)?.apply {
                            this[imageIndex] = image
                            notifyItemChanged(index)
                        }
                        return
                    }
                }
                (monthData.images as? ArrayList)?.apply {
                    this.add(0, image)
                    notifyItemChanged(index)
                }
                return
            }
        }
        data.add(0, SearchMonthData(arrayListOf(image)))
        notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }

    override fun onBind(binding: ItemTitleScrollBinding, viewData: SearchMonthData, position: Int) {
        binding.showTitle = true
        binding.dateHint = viewData.getMonthHint()
        binding.listView.adapter = viewData.adapter
        viewData.adapter.onBind(viewData.images,data)
        if (binding.listView.layoutManager == null){
            binding.listView.layoutManager = GridLayoutManager(binding.root.context,3)
        }
    }
}