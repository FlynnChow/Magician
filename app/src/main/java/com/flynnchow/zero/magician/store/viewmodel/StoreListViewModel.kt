package com.flynnchow.zero.magician.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flynnchow.zero.common.viewmodel.BaseViewModel
import com.flynnchow.zero.database.RoomManager
import com.flynnchow.zero.model.StoreModel
import com.google.gson.Gson

class StoreListViewModel:BaseViewModel() {
    private var newCreateUris:List<String>? = null
    private var newCreateTitle:String = ""
    private val _stores = MutableLiveData<List<StoreModel>>()
    val stores:LiveData<List<StoreModel>> = _stores
    private val _newStore = MutableLiveData<StoreModel>()
    val newStore:LiveData<StoreModel> = _newStore

    fun initData(){
        startLaunch {
            _stores.value = RoomManager.instance.getStoreModelDao().getData()
        }
    }

    fun createToTitle(title:String){
        newCreateTitle = title
    }

    fun createToImages(uri: List<String>){
        newCreateUris = uri
    }

    suspend fun createNewStore():Int?{
        newCreateUris?.run {
            val uriToJson = Gson().toJson(this)
            var store = StoreModel(newCreateTitle,uriToJson,first())
            val dao = RoomManager.instance.getStoreModelDao()
            dao.replaceInsert(store)
            store = RoomManager.instance.getStoreModelDao().getData().first()
            _newStore.value = store
            return store.id
        }
        return null
    }
}