package com.flynnchow.zero.magician.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemTitleScrollBinding
import com.flynnchow.zero.magician.gallery.viewHolder.GalleryMonthHolder
import com.flynnchow.zero.magician.gallery.viewdata.GalleryDayData
import com.flynnchow.zero.magician.gallery.viewdata.GalleryMonthData
import com.flynnchow.zero.model.MediaModel

class GalleryMainAdapter(var type: Int) : RecyclerView.Adapter<GalleryMonthHolder>() {
    private val _data: ArrayList<GalleryMonthData> = ArrayList()
    private var isFirstInit = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryMonthHolder {
        val itemBinding: ItemTitleScrollBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_title_scroll,
            parent,
            false
        )
        return GalleryMonthHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GalleryMonthHolder, position: Int) {
        holder.bindData(_data[position], type)
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    fun setData(data: List<GalleryMonthData>) {
        if (!isFirstInit) {
            return
        }
        isFirstInit = false
        _data.clear()
        _data.addAll(data)
        notifyDataSetChanged()
    }

    fun updateData(data: MediaModel,recyclerView: RecyclerView) {
        val dayHint = GalleryDayData.getDayHint(data)
        val monthHint = GalleryDayData.getMonthHint(data)
        for (index in 0 until itemCount) {
            val monthData = _data[index]
            if (monthData.getMonthHint() == monthHint) {
                for (dayData in monthData.data) {
                    if (dayHint == dayData.getDayHint()) {
                        for (imageIndex in dayData.images.indices) {
                            val image = dayData.images[imageIndex]
                            if (data.id == image.id) {
                                (dayData.images as? ArrayList)?.apply {
                                    this[imageIndex] = data
                                    notifyItemChanged(index)
                                }
                                return
                            }
                        }
                        (dayData.images as? ArrayList)?.apply {
                            this.add(0, data)
                            notifyItemChanged(index)
                        }
                        return
                    }
                }
            }
        }
        _data.add(0, GalleryMonthData(arrayListOf(data)))
        notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }
}