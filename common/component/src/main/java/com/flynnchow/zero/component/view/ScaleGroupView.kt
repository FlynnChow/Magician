package com.flynnchow.zero.component.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.LinearLayout

class ScaleGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : LinearLayout(context, attrs, style),ScaleGestureDetector.OnScaleGestureListener {
    private val scaleDetector = ScaleGestureDetector(context,this)
    private var isScale = false

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        return false
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        isScale = true
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        isScale = false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(ev)
        return if(!isScale){
            super.dispatchTouchEvent(ev)
        }else{
            true
        }
    }
}