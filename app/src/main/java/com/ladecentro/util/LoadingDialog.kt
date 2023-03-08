package com.ladecentro.util

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import com.ladecentro.R

class LoadingDialog(activity: Activity) {

    private var alertDialog : AlertDialog

    init {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)
        alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
    }

    fun startLoading() {
        alertDialog.show()
    }

    fun stopLoading() {
        alertDialog.dismiss()
    }
}