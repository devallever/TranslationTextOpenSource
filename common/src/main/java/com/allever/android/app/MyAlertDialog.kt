package com.allever.android.app

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class MyAlertDialog: AlertDialog {
    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

     class Builder: AlertDialog.Builder {
         constructor(context: Context) : super(context)
         constructor(context: Context, themeResId: Int) : super(context, themeResId)
     }
}