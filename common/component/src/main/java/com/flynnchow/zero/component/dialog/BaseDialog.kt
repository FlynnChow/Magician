package com.flynnchow.zero.component.dialog

abstract class BaseDialog {
    var isSpaceCancel = false

    abstract fun show()

    abstract fun dismiss()
}