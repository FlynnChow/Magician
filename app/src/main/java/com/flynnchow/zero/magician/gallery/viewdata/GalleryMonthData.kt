package com.flynnchow.zero.magician.gallery.viewdata

import com.flynnchow.zero.model.MediaModel

class GalleryMonthData(images: List<MediaModel>) {
    val data: List<GalleryDayData>

    init {
        data = ArrayList()
        if (images.isNotEmpty()) {
            var dayValue = images[0].getDay()
            var monthValue = images[0].getMonth()
            var yearValue = images[0].getYear()
            var imageList = ArrayList<MediaModel>()
            for (image in images) {
                if (image.getYear() != yearValue || image.getMonth() != monthValue || image.getDay() != dayValue) {
                    val monthBean = GalleryDayData(imageList)
                    data.add(monthBean)
                    dayValue = image.getDay()
                    monthValue = image.getMonth()
                    yearValue = image.getYear()
                    imageList = ArrayList()
                }
                imageList.add(image)
            }
            if (imageList.isNotEmpty()) {
                val monthBean = GalleryDayData(imageList)
                data.add(monthBean)
            }
        }
    }

    fun getMonthHint() = data[0].getMonthHint()
}