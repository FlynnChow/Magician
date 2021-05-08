package com.flynnchow.zero.magician.store.viewdata

import android.net.Uri
import com.flynnchow.zero.model.StoreModel
import java.text.SimpleDateFormat
import java.util.*

class StoreViewData(val data: StoreModel) {
    fun getUri(): Uri = Uri.parse(data.image)
}