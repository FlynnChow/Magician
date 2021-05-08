package com.flynnchow.zero.ml_kit

import com.flynnchow.zero.model.MediaModel
import com.flynnchow.zero.model.MlResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MLKitHelper {
    suspend fun isHitLabel(media: MediaModel, target: String): Boolean {
        return withContext(Dispatchers.Default) {
            val mlResult = media.mlResult
            if (mlResult != null) {
                testMlHit(target, mlResult)
            } else false
        }
    }

    private fun testMlHit(target: String, result: MlResult): Boolean {
        if (result.toString().contains(target)) {
            return true
        }
        if (target == "动物") {
            result.result?.let {
                for (label in it) {
                    if (label.mlType == MLKitManager.ML_MODEL_ML_KIT && containerGroup(
                            label.index, arrayOf(278, 360, 118)
                        )
                    )
                        return true
                    if (label.mlType == MLKitManager.ML_MODEL_IMAGE_NET && containerRouge(
                            label.index, arrayOf(1, 398)
                        )
                    )
                        return true
                }
            }
        }
        if (target == "人物" || target == "人类") {
            result.result?.let {
                for (label in it) {
                    if (label.mlType == MLKitManager.ML_MODEL_ML_KIT && containerGroup(
                            label.index,
                            arrayOf(66, 130, 131, 146, 163, 170, 207, 218, 258, 361, 421, 438)
                        )
                    )
                        return true
                    if (label.mlType == MLKitManager.ML_MODEL_IMAGE_NET && containerGroup(
                            label.index,
                            arrayOf(440)
                        )
                    )
                        return true
                }
            }
        }
        if (target == "美食" || target == "食物" || target == "吃的") {
            result.result?.let {
                for (label in it) {
                    if (label.mlType == MLKitManager.ML_MODEL_ML_KIT && containerGroup(
                            label.index,
                            arrayOf(48, 50, 126, 133, 162, 187, 300, 320, 331, 332, 433)
                        )
                    )
                        return true
                    if (label.mlType == MLKitManager.ML_MODEL_IMAGE_NET && containerRouge(
                            label.index, arrayOf(925, 936, 960, 968)
                        )
                    )
                        return true
                }
            }
        }
        if (target == "水果") {
            result.result?.let {
                for (label in it) {
                    if (label.mlType == MLKitManager.ML_MODEL_ML_KIT && containerGroup(
                            label.index,
                            arrayOf(187)
                        )
                    )
                        return true
                    if (label.mlType == MLKitManager.ML_MODEL_IMAGE_NET && containerRouge(
                            label.index, arrayOf(949, 959)
                        )
                    )
                        return true
                }
            }
        }
        if (target == "蔬菜") {
            result.result?.let {
                for (label in it) {
                    if (label.mlType == MLKitManager.ML_MODEL_ML_KIT && containerGroup(
                            label.index,
                            arrayOf(389)
                        )
                    )
                        return true
                    if (label.mlType == MLKitManager.ML_MODEL_IMAGE_NET && containerRouge(
                            label.index, arrayOf(936, 947)
                        )
                    )
                        return true
                }
            }
        }
        return false
    }

    private fun containerGroup(index: Int, intArray: Array<Int>): Boolean {
        return intArray.contains(index)
    }

    private fun containerRouge(target: Int, intArray: Array<Int>): Boolean {
        for (index in 1 until intArray.size step 2) {
            if (target in intArray[index - 1]..intArray[index]) {
                return true
            }
        }
        return false
    }
}