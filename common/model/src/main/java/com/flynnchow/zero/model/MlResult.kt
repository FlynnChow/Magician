package com.flynnchow.zero.model

import java.lang.StringBuilder

class MlResult {
    var createDate: Long = System.currentTimeMillis()
    var result: List<MlLabel>? = null

    private var toStringResult: String? = null
    override fun toString(): String {
        if (toStringResult == null) {
            val strBuild = StringBuilder()
            result?.run {
                for (label in this) {
                    if (strBuild.isNotEmpty())
                        strBuild.append(",")
                    strBuild.append("[").append(label.label).append("]")
                }
            }
            toStringResult = strBuild.toString()
        }
        return toStringResult ?: ""
    }
}
