package com.flynnchow.zero.ml_kit

import com.flynnchow.zero.model.MediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MLKitHelper {
    suspend fun isHitLabel(media:MediaModel,target: String):Boolean{
        return withContext(Dispatchers.Default){
            val mlResult = media.mlResult
            if(mlResult != null){
                val mlResultStr = mlResult.toString()
                mlResultStr.contains(target)
            }
            else false
        }
    }
}