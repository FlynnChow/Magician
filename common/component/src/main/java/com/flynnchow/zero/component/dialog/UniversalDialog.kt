package com.flynnchow.zero.component.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.flynnchow.zero.component.R
import com.flynnchow.zero.component.databinding.DialogUniversalBinding
import com.flynnchow.zero.component.view.dp

class UniversalDialog(activity: Activity,private val content:String) {
    private val dialog = Dialog(activity, R.style.CustomizeDialog)
    private var confirm:(()->Unit)? = null

    private val dialogBinding = DataBindingUtil.inflate<DialogUniversalBinding>(
        activity.layoutInflater,
        R.layout.dialog_universal,
        null,
        false
    )

    init {
        dialog.setContentView(dialogBinding.root)
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.y = 12.dp
        window?.setWindowAnimations(R.style.DialogAnim)
        dialog.setCanceledOnTouchOutside(true)

        dialogBinding.content = content
        dialogBinding.dialogCancel.setOnClickListener {
            dismiss()
        }
        dialogBinding.dialogConfirm.setOnClickListener {
            confirm?.invoke()
            dismiss()
        }
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

    fun addConfirmListener(func:()->Unit):UniversalDialog{
        confirm = func
        return this
    }
}