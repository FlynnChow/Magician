package com.flynnchow.zero.magician.store.adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemListStoreBinding
import com.flynnchow.zero.magician.store.viewdata.StoreViewData
import com.flynnchow.zero.magician.store.view.StoreActivity
import com.flynnchow.zero.model.StoreModel

class StoreListAdapter(private val activity:Activity) :
    DataBindAdapter<ItemListStoreBinding, StoreViewData>(R.layout.item_list_store) {
    override fun onBind(binding: ItemListStoreBinding, viewData: StoreViewData, position: Int) {
        binding.viewData = viewData
        binding.root.setOnClickListener {
            StoreActivity.startStorePage(activity,binding.image,viewData.data.id)
        }
    }

    fun addData(model: StoreModel) {
        val addIndex = itemCount
        data.add(StoreViewData(model))
        notifyItemInserted(addIndex)
    }

    fun removeData(id: Int) {
        for ((index,item) in data.withIndex()){
            if (item.data.id == id){
                data.removeAt(index)
                notifyItemRemoved(index)
                break
            }
        }
    }

    fun addFirstData(model: StoreModel,recyclerView: RecyclerView) {
        data.add(0,StoreViewData(model))
        notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }

    fun addData(modes: List<StoreModel>) {
        val addIndex = itemCount
        data.addAll(modes.map { StoreViewData(it) })
        notifyItemRangeInserted(addIndex,modes.size)
    }
}