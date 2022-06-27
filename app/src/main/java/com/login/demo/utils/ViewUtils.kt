package com.login.demo.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.login.demo.R
import com.login.demo.appview.country_picker.view.CountryPickerFragment
import com.login.demo.appview.authentication.home.view.HomeActivity
import com.login.demo.customview.CustomDialog
import kotlinx.android.synthetic.main.popup_title.view.*

object ViewUtils {

    fun showTitlePopUp(v: View, popUpItemClickListener: PopUpItemClickListener) {
        var popUpWindow: PopupWindow? = null
        val inflater =
            v.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_title, null)

        view.txtMr.setOnClickListener {
            popUpWindow?.dismiss()
            popUpItemClickListener.selectedItem(v.context.resources.getString(R.string.str_mr))
        }

        view.txtMrs.setOnClickListener {
            popUpWindow?.dismiss()
            popUpItemClickListener.selectedItem(v.context.resources.getString(R.string.str_mrs))
        }

        view.txtMiss.setOnClickListener {
            popUpWindow?.dismiss()
            popUpItemClickListener.selectedItem(v.context.resources.getString(R.string.str_miss))
        }

        view.txtDr.setOnClickListener {
            popUpWindow?.dismiss()
            popUpItemClickListener.selectedItem(v.context.resources.getString(R.string.str_dr))
        }

        popUpWindow = PopupWindow(
            view,
            v.context.resources.getDimension(R.dimen._60sdp).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popUpWindow.showAsDropDown(v, -50, 0)
    }

    fun showCustomDialog(
        activity: Activity,
        titleText: String,
        instructionText: String,
        btnPositiveText: String?,
        btnNegativeText: String?,
        btnClickListener: CustomDialog.ButtonClickListener
    ) {
        val dialog = CustomDialog(
            activity = activity,
            titleText = titleText,
            instructionText = instructionText,
            btnPositiveText = btnPositiveText,
            btnNegativeText = btnNegativeText,
            btnClickListener = btnClickListener
        )
        dialog.show(
            (activity as HomeActivity).supportFragmentManager,
            dialog.tag
        )
    }

    fun showCountryPicker(context: Context, countrySelectedListener: CountrySelectedListener) {
        val countryPickerFragment = CountryPickerFragment()
        countryPickerFragment.setCountrySelectedListener(object :
            CountrySelectedListener {
            override fun onCountrySelect(countryCode: String) {
                countryPickerFragment.dismiss()
                countrySelectedListener.onCountrySelect(countryCode)
            }
        })
        countryPickerFragment.show(
            (context as HomeActivity).supportFragmentManager,
            countryPickerFragment.tag
        )
    }



    interface DialogDismiss {
        fun onDialogDismiss()
    }

    interface PopUpItemClickListener {
        fun selectedItem(selectedItem: String)
    }



    interface CountrySelectedListener {
        fun onCountrySelect(countryCode: String)
    }


}