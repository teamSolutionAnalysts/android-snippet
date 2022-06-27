package com.login.demo.customview

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.login.base.base.BaseDialogFragment
import com.login.base.base.IFragmentState
import com.login.demo.R
import com.login.demo.databinding.LayoutCustomDialogBinding
import kotlinx.android.synthetic.main.layout_custom_dialog.*

class CustomDialog(
    var activity: Activity,
    var titleText: String,
    var instructionText: String,
    var btnPositiveText: String?,
    var btnNegativeText: String?,
    var btnClickListener: ButtonClickListener
) : BaseDialogFragment(), View.OnClickListener {

    private lateinit var mFragmentBinding: LayoutCustomDialogBinding

    override fun getLayoutId(): Int {
        dialog?.requestWindowFeature(STYLE_NO_TITLE)
        return R.layout.layout_custom_dialog
    }

    override fun preDataBinding(arguments: Bundle?) {

    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        mFragmentBinding = binding as LayoutCustomDialogBinding
        mFragmentBinding.lifecycleOwner = this
        return mFragmentBinding
    }

    override fun initializeComponent(view: View?) {
        titleTxt.text = titleText
        instructionTxt.text = instructionText
        btnPositive.text = btnPositiveText
        if (!btnNegativeText.isNullOrEmpty()) {
            btnNegative.visibility = View.VISIBLE
            btnNegative.text = btnNegativeText
        } else {
            btnNegative.visibility = View.GONE
        }

        btnPositive.setOnClickListener {
            btnClickListener.onPositiveClick(this)
        }

        btnNegative.setOnClickListener {
            btnClickListener.onNegativeClick(this)
        }
    }

    override fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?) {

    }

    override fun pageVisible() {

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }

    interface ButtonClickListener {
        fun onPositiveClick(dialog: CustomDialog)

        fun onNegativeClick(dialog: CustomDialog)
    }

}