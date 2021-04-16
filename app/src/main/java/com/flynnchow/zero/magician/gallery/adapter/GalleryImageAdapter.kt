package com.flynnchow.zero.magician.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.gallery.GalleryType
import com.flynnchow.zero.magician.gallery.viewHolder.GalleryImageHolder
import com.flynnchow.zero.model.MediaModel
import java.lang.Exception

class GalleryImageAdapter(private var _type:Int):RecyclerView.Adapter<GalleryImageHolder>() {
    private var _data: ArrayList<MediaModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageHolder {
        val itemBinding: ViewDataBinding = when(viewType){
            GalleryType.MONTH_FIVE->{
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_image_mini,
                    parent,
                    false
                )
            }
            GalleryType.DAY_THREE->{
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_image_medium,
                    parent,
                    false
                )
            }
            GalleryType.DAY_ONE->{
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_image_origin,
                    parent,
                    false
                )
            }
            else-> throw Exception("type not exit")
        }
        return GalleryImageHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: GalleryImageHolder, position: Int) {
        holder.bindData(_data[position])
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    override fun getItemViewType(position: Int): Int {
        return _type
    }

    fun onBind(type:Int, data:List<MediaModel>){
        _type = type
        _data = data as ArrayList<MediaModel>
    }
}