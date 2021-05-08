package com.flynnchow.zero.component.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.flynnchow.zero.component.R
import com.flynnchow.zero.component.databinding.DialogLoadingBinding
import com.flynnchow.zero.component.view.dp

class LoadingDialog(activity: Activity, private val content:String)  {
    companion object{
        private var dialog:LoadingDialog? = null
        fun show(activity: Activity, content:String){
            if (dialog != null){
                dialog?.dismiss()
                dialog = null
            }
            dialog = LoadingDialog(activity,content)
            dialog?.show()
        }
        fun dismiss(){
            dialog?.dismiss()
            dialog = null
        }
    }
    private val dialog = Dialog(activity, R.style.CustomizeDialog)

    private val dialogBinding = DataBindingUtil.inflate<DialogLoadingBinding>(
        activity.layoutInflater,
        R.layout.dialog_loading,
        null,
        false
    )

    init {
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams?.gravity = Gravity.CENTER
        layoutParams?.y = 12.dp
        window?.setWindowAnimations(R.style.DialogAnim)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        dialogBinding.content = content
    }

    fun show(){
        if (!dialog.isShowing){
            dialog.show()
        }
    }

    fun dismiss(){
        if (dialog.isShowing){
            dialog.dismiss()
        }
    }
}