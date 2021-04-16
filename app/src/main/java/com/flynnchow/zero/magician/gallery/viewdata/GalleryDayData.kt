package com.flynnchow.zero.magician.gallery.viewdata

import com.flynnchow.zero.model.MediaModel
import java.util.*

class GalleryDayData(val images: List<MediaModel>) {
    companion object {
        fun getDayHint(image: MediaModel) = "${getMonthHint(image)}${image.getDay()}日"

        fun getMonthHint(image: MediaModel) = "${getYear(image)}${image.getMonth()}月"

        private fun getYear(image: MediaModel): String {
            val year = image.getYear()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            if (year != calendar.get(Calendar.YEAR)) {
                return "${year}年"
            }
            return ""
        }
    }

    fun getDayHint() = getDayHint(images[0])

    fun getMonthHint() = getMonthHint(images[0])
}