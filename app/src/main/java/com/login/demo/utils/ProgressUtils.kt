package com.login.demo.utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.login.demo.R

object ProgressUtils {

    private var progressDialog: ProgressDialog? = null

    val isShowing: Boolean
        get() = progressDialog != null

    fun showProgressIndicator(context: Context?) {
        if (context != null && progressDialog == null || !progressDialog!!.isShowing) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.isIndeterminate = true
            progressDialog?.show()
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog?.setContentView(R.layout.layout_custom_progressindicator)
        }

    }

    fun closeProgressIndicator() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog?.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            progressDialog = null
        }
    }
}