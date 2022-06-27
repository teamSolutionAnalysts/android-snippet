package com.login.base.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar


object SnackBarUtils {
    fun show(view: View, description: Int) {
        show(view, description, Snackbar.LENGTH_LONG)
    }

    fun show(
        view: View,
        description: Int,
        duration: Int
    ) {
        Snackbar.make(view, description, duration).show()
    }

    fun show(view: View, description: String) {
        show(view, description, Snackbar.LENGTH_SHORT)
    }

    fun show(
        view: View,
        description: String,
        duration: Int
    ) {
        Snackbar.make(view, description, duration).show()
    }
}