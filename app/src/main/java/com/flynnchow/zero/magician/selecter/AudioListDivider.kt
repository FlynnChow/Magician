package com.flynnchow.zero.magician.selecter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.component.view.dp
import com.flynnchow.zero.magician.R


class AudioListDivider : RecyclerView.ItemDecoration() {
    private val mDividerHeight = 1.dp
    private val mDivider =
        ContextCompat.getDrawable(BaseApplication.instance, R.drawable.shape_divider)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        var top = 0
        var dividerHeight = 0
        if(parent.getChildLayoutPosition(view) == 0){
            top = 12.dp
        }
        if (parent.getChildLayoutPosition(view) < parent.childCount - 1){
            dividerHeight = mDividerHeight
        }
        outRect.set(0, top, 0, dividerHeight)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager && layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            drawDivider(canvas, parent)
        }
    }

    private fun drawDivider(canvas: Canvas, parent: RecyclerView) {
        val left: Int = parent.paddingLeft + 120.dp
        val right: Int = parent.measuredWidth - parent.paddingRight
        val childSize: Int = parent.childCount
        for (i in 0 until childSize) {
            val child: View = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom: Int = top + mDividerHeight
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }
        }
    }
}