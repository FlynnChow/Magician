package com.flynnchow.zero.magician.gallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.magician.gallery.viewdata.GalleryMonthData
import com.flynnchow.zero.model.MediaModel

class GalleryViewModel : BaseViewModel() {
    private val _data = MutableLiveData<List<GalleryMonthData>>()
    val data: LiveData<List<GalleryMonthData>> = _data

    fun initGallery(images: List<MediaModel>) {
        val monthList = ArrayList<GalleryMonthData>()
        if (images.isNotEmpty()) {
            var monthValue = images[0].getMonth()
            var monthYear = images[0].getYear()
            var dayList = ArrayList<MediaModel>()
            for (image in images) {
                if (image.getYear() != monthYear || image.getMonth() != monthValue) {
                    val monthBean = GalleryMonthData(dayList)
                    monthList.add(monthBean)
                    monthValue = image.getMonth()
                    monthYear = image.getYear()
                    dayList = ArrayList()
                    dayList.add(image)
                }
                dayList.add(image)
            }
            if (dayList.isNotEmpty()) {
                val monthBean = GalleryMonthData(dayList)
                monthList.add(monthBean)
            }
            _data.value = monthList
        }
    }
}