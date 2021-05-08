package com.flynnchow.zero.magician.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.search.viewdata.SearchMonthData
import com.flynnchow.zero.ml_kit.MLKitHelper
import com.flynnchow.zero.model.MediaModel

class SearchViewModel : BaseViewModel() {
    var keyword = ""
    private val _data = MutableLiveData<List<SearchMonthData>>()
    val data: LiveData<List<SearchMonthData>> = _data

    fun onSearch(keyword: String) {
        this.keyword = keyword
        initSearchData()
    }

    private fun initSearchData() {
        startLaunch {
            val images = if (keyword.trim().isNotEmpty()) {
                MediaProvider.instance.getImageList().filter {
                    MLKitHelper.isHitLabel(it, keyword)
                }
            } else {
                MediaProvider.instance.getImageList()
            }
            val monthList = ArrayList<SearchMonthData>()
            if (images.isNotEmpty()) {
                var monthValue = images[0].getMonth()
                var monthYear = images[0].getYear()
                var dayList = ArrayList<MediaModel>()
                for (image in images) {
                    if (image.getYear() != monthYear || image.getMonth() != monthValue) {
                        val monthBean = SearchMonthData(dayList)
                        monthList.add(monthBean)
                        monthValue = image.getMonth()
                        monthYear = image.getYear()
                        dayList = ArrayList()
                        dayList.add(image)
                    }
                    dayList.add(image)
                }
                if (dayList.isNotEmpty()) {
                    val monthBean = SearchMonthData(dayList)
                    monthList.add(monthBean)
                }
                _data.value = monthList
            }else{
                _data.value = ArrayList()
            }
        }
    }
}