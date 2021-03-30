package com.flynnchow.zero.common.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.flynnchow.zero.base.BaseApplication

object ViewUtils {
    @JvmStatic
    fun showSoftInput(context: Context, view: View){
        view.requestFocus()
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
}

fun Int.dp() = this.toFloat().dp()

fun Double.dp() = this.toFloat().dp()

fun Float.dp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, BaseApplication.instance.resources?.displayMetrics)

fun Int.sp() = this.toFloat().sp()

fun Double.sp() = this.toFloat().sp()

fun Float.sp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, BaseApplication.instance.resources?.displayMetrics)