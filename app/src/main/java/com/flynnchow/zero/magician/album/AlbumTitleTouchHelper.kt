package com.flynnchow.zero.magician.album

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.magician.album.adapter.AlbumTitleAdapter
import java.util.*

class AlbumTitleTouchHelper(private val adapter: AlbumTitleAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.bindingAdapterPosition //得到拖动ViewHolder的position
        val toPosition = target.bindingAdapterPosition //得到目标ViewHolder的position
        if (fromPosition < toPosition) {
            when(adapter.checkPosition){
                toPosition -> adapter.checkPosition = fromPosition
                fromPosition -> adapter.checkPosition = toPosition
                in fromPosition..toPosition -> adapter.checkPosition--
            }
            for (index in fromPosition until toPosition) {
                Collections.swap(adapter.data, index, index + 1)
            }
            adapter.notifyItemMoved(fromPosition, toPosition)
            adapter.notifyItemRangeChanged(fromPosition, toPosition - fromPosition + 1)
        } else {
            when(adapter.checkPosition){
                toPosition -> adapter.checkPosition = fromPosition
                fromPosition -> adapter.checkPosition = toPosition
                in fromPosition..toPosition -> adapter.checkPosition++
            }
            for (index in fromPosition downTo toPosition + 1) {
                Collections.swap(adapter.data, index, index - 1)
            }
            adapter.notifyItemMoved(fromPosition, toPosition)
            adapter.notifyItemRangeChanged(toPosition, fromPosition - toPosition + 1)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}