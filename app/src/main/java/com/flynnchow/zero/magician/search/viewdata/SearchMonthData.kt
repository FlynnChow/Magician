package com.flynnchow.zero.magician.search.viewdata

import com.flynnchow.zero.magician.gallery.viewdata.GalleryDayData
import com.flynnchow.zero.magician.search.adapter.SearchImageAdapter
import com.flynnchow.zero.model.MediaModel

class SearchMonthData(val images: List<MediaModel>) {
    val adapter = SearchImageAdapter()

    fun getMonthHint() = GalleryDayData.getMonthHint(images[0])
}