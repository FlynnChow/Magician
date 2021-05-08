package com.flynnchow.zero.component.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.component.R
import com.flynnchow.zero.component.databinding.DialogLoadingBinding
import com.flynnchow.zero.component.databinding.DialogProgressBinding
import com.flynnchow.zero.component.view.dp

class ProgressDialog(
    activity: Activity,
    private val title: String,
    private val rightButtonHint: String
) {
    private val dialog = Dialog(activity, R.style.CustomizeDialog)

    private val dialogBinding = DataBindingUtil.inflate<DialogProgressBinding>(
        activity.layoutInflater,
        R.layout.dialog_progress,
        null,
        false
    )

    private var cancelListener:(()->Unit)? = null
    private var confirmListener:(()->Unit)? = null

    init {
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.y = 12.dp
        window?.setWindowAnimations(R.style.DialogAnim)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        dialogBinding.title = title
        dialogBinding.message = ""
        dialogBinding.rightButtonHint = rightButtonHint
        dialogBinding.progress = 0
        dialogBinding.dialogCancel.setOnClickListener {
            cancelListener?.invoke()
            dismiss()
        }
        dialogBinding.dialogConfirm.setOnClickListener {
            confirmListener?.invoke()
        }
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun addCancelListener(listener:(()->Unit)){
        cancelListener = listener
    }

    fun addConfirmListener(listener:(()->Unit)){
        confirmListener = listener
    }

    fun setProgress(progress:Int){
        dialogBinding.progress = progress
    }

    fun setMessage(message:String){
        dialogBinding.message = message
    }

    fun setProgressVisibility(visibility:Boolean){
        val progressVisible = if (visibility) View.VISIBLE else View.GONE
        val loadingVisible = if (visibility) View.GONE else View.VISIBLE
        if (dialogBinding.progressBar.visibility != progressVisible)
            dialogBinding.progressBar.visibility = progressVisible
        if (dialogBinding.progressHint.visibility != progressVisible)
            dialogBinding.progressHint.visibility = progressVisible
        if (dialogBinding.progressBarAnim.visibility != loadingVisible)
            dialogBinding.progressBarAnim.visibility = loadingVisible
    }

    fun isConfirmClickable(clickable:Boolean){
        dialogBinding.dialogConfirm.isClickable = clickable
        if (clickable){
            dialogBinding.dialogConfirm.setTextColor(BaseApplication.instance.resources.getColor(R.color.font_black))
        }else{
            dialogBinding.dialogConfirm.setTextColor(BaseApplication.instance.resources.getColor(R.color.font_white_3))
        }
    }

    fun isCancelClickable(clickable:Boolean){
        dialogBinding.dialogCancel.isClickable = clickable
        if (clickable){
            dialogBinding.dialogCancel.setTextColor(BaseApplication.instance.resources.getColor(R.color.font_black))
        }else{
            dialogBinding.dialogCancel.setTextColor(BaseApplication.instance.resources.getColor(R.color.font_white_3))
        }
    }
}