package com.flynnchow.zero.component.view

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.BaseApplication

object ViewUtils {
    @JvmStatic
    fun showSoftInput(view: View){
        view.requestFocus()
        val imm: InputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    @JvmStatic
    fun hideSoftInput(context: Activity){
        val view: View? = context.currentFocus
        if (view != null) {
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    @JvmStatic
    fun scrollToEnd(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>){
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    @JvmStatic
    fun getPxFromDp(value:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, BaseApplication.instance.resources?.displayMetrics).toInt()
    }

    @JvmStatic
    fun getPxFromSp(value:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, BaseApplication.instance.resources?.displayMetrics).toInt()
    }
}

val Int.dp: Int
    get() = ViewUtils.getPxFromDp(this.toFloat())

val Float.dp: Int
    get() = ViewUtils.getPxFromDp(this)

val Double.dp: Int
    get() = ViewUtils.getPxFromDp(this.toFloat())

val Long.dp: Int
    get() = ViewUtils.getPxFromDp(this.toFloat())

val Int.sp: Int
    get() = ViewUtils.getPxFromSp(this.toFloat())

val Float.sp: Int
    get() = ViewUtils.getPxFromSp(this)

val Double.sp: Int
    get() = ViewUtils.getPxFromSp(this.toFloat())

val Long.sp: Int
    get() = ViewUtils.getPxFromSp(this.toFloat())