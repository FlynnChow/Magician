package com.flynnchow.zero.ml_kit

import android.content.Context
import android.net.Uri
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.MlLabel
import com.flynnchow.zero.model.MlResult
import com.google.gson.Gson
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.*

class MLKitManager {
    companion object {
        const val ML_MODEL_ML_KIT = "ml_kit"
        const val ML_MODEL_IMAGE_NET = "image_net"

        //最低识别率
        const val minConfidenceThreshold = 0.75f

        private var instance: MLKitManager? = null

        private suspend fun initMLKitManager(): MLKitManager {
            return MLKitManager().apply {
                withContext(Dispatchers.IO) {
                    initMLKitLabel()
                    initImageNetLabel()
                    initMLLabeler()
                }
                instance = this
            }
        }

        suspend fun getInstance(): MLKitManager {
            return if (instance == null) {
                initMLKitManager()
            } else
                instance!!
        }
    }

    private lateinit var imageNetLabel: List<String>
    private lateinit var mlKitLabel: List<String>
    private lateinit var imageNetLabeler: ImageLabeler
    private lateinit var mlKitLabeler: ImageLabeler

    suspend fun doAsync(data: MediaModel, callback: (MediaModel?) -> Unit) {
        withContext(Dispatchers.Default){
            val labelList = ArrayList<MlLabel>()
            val inputData = data.clone()
            try {
                val image = InputImage.fromFilePath(BaseApplication.instance, inputData.getUri())
                asyncToMLKit(image, labelList) {
                    onAsyncResult(inputData, labelList, callback)
                }
            } catch (e: Exception) {
                onAsyncResult(inputData, null, callback)
            }
        }
    }

    private fun asyncToMLKit(
        srcImage: InputImage,
        result: ArrayList<MlLabel>,
        callback: () -> Unit
    ) {
        mlKitLabeler.process(srcImage)
            .addOnSuccessListener { labels ->
                for (label in labels) {
                    val tag = getMLName(ML_MODEL_ML_KIT, label.index)
                    if (tag == "null")
                        continue
                    val mlLabel = MlLabel(label.index, tag, label.confidence, ML_MODEL_ML_KIT)
                    result.add(mlLabel)
                }
                asyncToImageNet(srcImage, result, callback)
            }
            .addOnFailureListener { e ->
                throw Exception("ML-KIT-ERROR")
            }
    }

    private fun asyncToImageNet(
        srcImage: InputImage,
        result: ArrayList<MlLabel>,
        callback: () -> Unit
    ) {
        imageNetLabeler.process(srcImage)
            .addOnSuccessListener { labels ->
                for (label in labels) {
                    val tag = getMLName(ML_MODEL_IMAGE_NET, label.index)
                    if (tag == "null")
                        continue
                    val mlLabel = MlLabel(label.index, tag, label.confidence, ML_MODEL_IMAGE_NET)
                    result.add(mlLabel)
                }
                callback()
            }
            .addOnFailureListener { e ->
                throw Exception("ML-KIT-ERROR")
            }
    }

    private fun onAsyncResult(
        data: MediaModel,
        result: ArrayList<MlLabel>?,
        callback: (MediaModel?) -> Unit
    ) {
        if (result != null){
            val outputData = MlResult()
            outputData.result = result
            val outputJson = Gson().toJson(outputData)
            data.mlResultJson = outputJson
            callback.invoke(data) //success
        }else{
            callback.invoke(null) //error
        }
    }

    private fun getMLName(type: String, index: Int): String {
        return when (type) {
            ML_MODEL_ML_KIT -> mlKitLabel[index]
            ML_MODEL_IMAGE_NET -> imageNetLabel[index]
            else -> "null"
        }
    }

    fun initImageNetLabel() {
        val resultList = ArrayList<String>(1001)
        var ins: InputStream? = null
        var bufferReader: BufferedReader? = null
        try {
            ins = BaseApplication.instance.assets.open("label/image_net_label.txt")
            bufferReader = BufferedReader(InputStreamReader(ins))
            while (true) {
                val strLine: String = bufferReader.readLine() ?: break
                resultList.add(strLine)
            }
            imageNetLabel = resultList
        } catch (e: IOException) {
            throw Exception("读取Image-Net标签错误")
        } finally {
            ins?.close()
            bufferReader?.close()
        }
    }

    fun initMLKitLabel() {
        val resultList = ArrayList<String>(446)
        var ins: InputStream? = null
        var bufferReader: BufferedReader? = null
        try {
            ins = BaseApplication.instance.assets.open("label/ml_kit_label.txt")
            bufferReader = BufferedReader(InputStreamReader(ins))
            while (true) {
                val strLine: String = bufferReader.readLine() ?: break
                resultList.add(strLine)
            }
            mlKitLabel = resultList
        } catch (e: IOException) {
            throw Exception("读取Image-Net标签错误")
        } finally {
            ins?.close()
            bufferReader?.close()
        }
    }

    fun initMLLabeler() {
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(minConfidenceThreshold)
            .build()
        mlKitLabeler = ImageLabeling.getClient(options)

        val localModel = LocalModel.Builder()
            .setAssetFilePath("ml/mobilenet.tflite")
            .build()
        val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
            .setConfidenceThreshold(minConfidenceThreshold)
            .build()
        imageNetLabeler = ImageLabeling.getClient(customImageLabelerOptions)
    }
}