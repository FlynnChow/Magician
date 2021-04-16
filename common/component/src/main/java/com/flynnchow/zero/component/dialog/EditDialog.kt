package com.flynnchow.zero.component.dialog

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.flynnchow.zero.component.R
import com.flynnchow.zero.component.databinding.DialogEditBinding
import com.flynnchow.zero.component.databinding.DialogUniversalBinding
import com.flynnchow.zero.component.view.ViewUtils
import com.flynnchow.zero.component.view.dp
import com.hjq.toast.ToastUtils
import kotlinx.coroutines.delay

class EditDialog(private val activity: Activity, private val title:String,private val hint:String,private val confirmHint:String) {
    private val dialog = Dialog(activity, R.style.CustomizeDialog)
    private var confirm:((String)->Unit)? = null

    private val dialogBinding = DataBindingUtil.inflate<DialogEditBinding>(
        activity.layoutInflater,
        R.layout.dialog_edit,
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

        dialogBinding.title = title
        dialogBinding.hint = hint
        dialogBinding.confirm = confirmHint

        dialogBinding.dialogCancel.setOnClickListener {
            dismiss()
        }
        dialogBinding.dialogConfirm.setOnClickListener {
            if (dialogBinding.edit.text.trim().isNotEmpty()){
                confirm?.invoke(dialogBinding.edit.text.toString())
                dismiss()
            }else{
                ToastUtils.show("未输入内容")
            }
        }
    }

    suspend fun show(){
        if (!dialog.isShowing){
            dialog.show()
            delay(200)
            ViewUtils.showSoftInput(dialogBinding.edit)
        }
    }

    fun dismiss(){
        if (dialog.isShowing){
            dialog.dismiss()
        }
    }

    fun addConfirmListener(func:(String)->Unit):EditDialog{
        confirm = func
        return this
    }
}