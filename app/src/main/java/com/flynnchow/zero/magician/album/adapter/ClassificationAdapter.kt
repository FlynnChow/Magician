package com.flynnchow.zero.magician.album.adapter

import com.flynnchow.zero.common.helper.ImageHelper
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.viewdata.ClassificationData
import com.flynnchow.zero.magician.databinding.ItemClassificationImageBinding
import com.flynnchow.zero.ml_kit.MLKitHelper
import com.flynnchow.zero.model.MediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClassificationAdapter(private val target: String) :
    DataBindAdapter<ItemClassificationImageBinding, ClassificationData>(R.layout.item_classification_image) {
    private var isFirstInit = true

    suspend fun updateData(media: MediaModel) {
        for (index in 0 until itemCount) {
            val viewData = data[index]
            if (media.id == viewData.data.id) {
                data[index] = ClassificationData(media)
                notifyItemChanged(index)
                return
            }
        }
        addData(media)
    }

    suspend fun addData(media: MediaModel) {
        if (MLKitHelper.isHitLabel(media,target)) {
            withContext(Dispatchers.Main){
                val addedIndex = itemCount
                data.add(ClassificationData(media))
                notifyItemInserted(addedIndex)
            }
        }
    }

    suspend fun addData(medias: List<MediaModel>) {
        val addedIndex = itemCount
        data.addAll(medias.filter {
            MLKitHelper.isHitLabel(it,target)
        }.map {
            ClassificationData(it)
        }.toList())
        notifyItemRangeInserted(addedIndex, medias.size)
    }

    suspend fun initData(medias: List<MediaModel>) {
        if (!isFirstInit) {
            return
        }
        isFirstInit = false
        addData(medias)
    }

    override fun onBind(
        binding: ItemClassificationImageBinding,
        viewData: ClassificationData,
        position: Int
    ) {
        binding.viewData = viewData
        binding.root.setOnClickListener {
            ImageHelper.viewImage(binding.image,data.map {
                it.data
            }.toTypedArray(),position)
        }
    }
}