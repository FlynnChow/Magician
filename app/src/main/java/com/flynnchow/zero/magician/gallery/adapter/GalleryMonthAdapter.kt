package com.flynnchow.zero.magician.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemTitleScrollBinding
import com.flynnchow.zero.magician.gallery.GalleryType
import com.flynnchow.zero.magician.gallery.viewHolder.GalleryDayHolder
import com.flynnchow.zero.magician.gallery.viewdata.GalleryDayData
import java.lang.Exception

class GalleryMonthAdapter(private var _type:Int): RecyclerView.Adapter<GalleryDayHolder>() {
    private var _data: ArrayList<GalleryDayData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):GalleryDayHolder {
        val itemBinding: ItemTitleScrollBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_title_scroll,
            parent,
            false
        )
        val layoutManager:RecyclerView.LayoutManager = when(viewType){
            GalleryType.DAY_ONE->{
                LinearLayoutManager(parent.context)
            }
            GalleryType.DAY_THREE->{
                GridLayoutManager(parent.context,3)
            }
            GalleryType.MONTH_FIVE->{
                GridLayoutManager(parent.context,5)
            }
            else -> throw Exception("type not exit")
        }
        return GalleryDayHolder(itemBinding,layoutManager)
    }

    override fun onBindViewHolder(holder: GalleryDayHolder, position: Int) {
        holder.bindData(_data[position],_type)
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    override fun getItemViewType(position: Int): Int {
        return _type
    }

    fun onBind(type:Int,data:List<GalleryDayData>){
        if (type != _type){
            _type = type
            notifyItemRangeChanged(0,itemCount)
        }
        _data = data as ArrayList<GalleryDayData>
    }
}