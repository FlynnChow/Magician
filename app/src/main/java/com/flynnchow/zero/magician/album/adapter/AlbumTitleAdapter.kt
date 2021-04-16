package com.flynnchow.zero.magician.album.adapter

import androidx.lifecycle.LifecycleOwner
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.viewdata.AlbumTitleData
import com.flynnchow.zero.magician.databinding.ItemAlbumTitleBinding
import com.flynnchow.zero.model.MLPhotoAlbum

class AlbumTitleAdapter(private val owner: LifecycleOwner) :
    DataBindAdapter<ItemAlbumTitleBinding, AlbumTitleData>(R.layout.item_album_title) {
    companion object {
        const val default = -1
        const val default_checked = 0
    }

    var checkPosition = default
    private var checkListener: ((AlbumTitleData) -> Unit)? = null


    override fun onBind(
        binding: ItemAlbumTitleBinding,
        viewData: AlbumTitleData,
        position: Int
    ) {
        viewData.position = position
        binding.viewData = viewData
        binding.isChecked = position == checkPosition
        binding.root.setOnClickListener {
            val oldPosition = checkPosition
            if (oldPosition != position) {
                checkPosition = position
                if (oldPosition != default) {
                    notifyItemChanged(oldPosition)
                }
                notifyItemChanged(checkPosition)
                checkListener?.invoke(viewData)
            }
        }
    }

    fun insertAlbum(album: MLPhotoAlbum) {
        val viewData = AlbumTitleData(album)
        if (checkPosition == default) {
            checkPosition = default_checked
            checkListener?.invoke(viewData)
        }
        val addIndex = itemCount
        data.add(viewData)
        notifyItemInserted(addIndex)
    }

    fun insertAlbum(viewData: AlbumTitleData) {
        if (checkPosition == default) {
            checkPosition = default_checked
            viewData.position = checkPosition
            checkListener?.invoke(viewData)
        }
        val addIndex = itemCount
        data.add(viewData)
        notifyItemInserted(addIndex)
    }

    fun insertAlbum(albums: List<MLPhotoAlbum>) {
        for (album in albums) {
            insertAlbum(album)
        }
    }

    fun removeAlbum(albums: List<MLPhotoAlbum>) {

    }

    fun removeAlbum(target: AlbumTitleData) {
        for (index in 0 until itemCount) {
            val viewData: AlbumTitleData = getData(index)
            if (viewData == target) {
                checkPosition = when {
                    index < itemCount - 1 -> {
                        index
                    }
                    checkPosition > 0 -> {
                        index - 1
                    }
                    else -> {
                        default
                    }
                }
                data.removeAt(index)
                notifyItemRemoved(index)
                notifyItemRangeRemoved(index, itemCount - index)
                if (default != checkPosition){
                    val nextData = data[checkPosition]
                    nextData.position = checkPosition
                    notifyItemChanged(checkPosition)
                    checkListener?.invoke(nextData)
                }
                break
            }
        }
    }

    fun addCheckListener(listener: ((AlbumTitleData) -> Unit)) {
        this.checkListener = listener
    }
}