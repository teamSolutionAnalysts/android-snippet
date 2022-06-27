package com.login.demo.utils


import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.login.demo.R
import com.login.demo.customview.CustomEditText
import kotlinx.android.synthetic.main.custom_edittext.view.*


object BindingAdapter {

    @BindingAdapter(
        value = ["app:customText", "app:edittextDataAttrChanged"],
        requireAll = false
    )
    @JvmStatic
    fun bindCustomEdittext(
        customEditText: CustomEditText,
        customText: String,
        txtAttrChanged: InverseBindingListener
    ) {
        customEditText.edittext.setText(customText)
        // Set the cursor to the end of the text
        var length: Int = 0
        customEditText.edittext.text?.let { editable ->
            length = editable.length
        }
        customEditText.edittext.setSelection(0)
        customEditText.edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtAttrChanged.onChange()
            }

        })
    }




    @InverseBindingAdapter(
        attribute = "app:customText",
        event = "app:edittextDataAttrChanged"
    )
    @JvmStatic
    fun captureEdittextData(customEditText: CustomEditText): String {
        return customEditText.edittext.text.toString()
    }

    @BindingAdapter(
        value = ["app:errText"],
        requireAll = false
    )
    @JvmStatic
    fun bindErrText(
        customEditText: CustomEditText,
        errText: String
    ) {
        if (errText.isNotEmpty()) {
            customEditText.txtError.visibility = View.VISIBLE
            customEditText.txtError.text = errText
        } else {
            customEditText.txtError.visibility = View.GONE
        }
    }

    @BindingAdapter(
        value = ["app:spinnerCustomText"],
        requireAll = false
    )
    @JvmStatic
    fun bindSpinnerText(
        customEditText: CustomEditText,
        spinnerCustomText: String
    ) {
        customEditText.txtSpinner.text = spinnerCustomText
    }



    @BindingAdapter(
        value = ["app:rightPasswordVisibility"],
        requireAll = false
    )
    @JvmStatic
    fun bindPasswordVisibility(
        customEditText: CustomEditText,
        isVerified: Int
    ) {
        val typeface = customEditText.edittext.typeface
        if (isVerified == 1) {
            customEditText.edittext.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            customEditText.iconRight.setImageDrawable(
                getDrawable(
                    customEditText.context,
                    R.drawable.ic_visibility
                )
            )
        } else {
            customEditText.edittext.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
            customEditText.iconRight.setImageDrawable(
                getDrawable(
                    customEditText.context,
                    R.drawable.ic_invisible
                )
            )
        }
        customEditText.edittext.typeface = typeface
    }



    @BindingAdapter("viewVisibility")
    @JvmStatic
    fun setViewVisibility(view: View, boolean: Boolean) {
        if (boolean) view.visibility = View.VISIBLE else view.visibility = View.GONE
    }



    @BindingAdapter(
        value = ["app:clickFullView"],
        requireAll = false
    )
    @JvmStatic
    fun bindEditDisabled(
        customEditText: CustomEditText,
        isEditable: Boolean
    ) {
        customEditText.edittext.isEnabled = isEditable
        customEditText.edittext.isCursorVisible = !isEditable
        customEditText.edittext.isFocusable = !isEditable
        customEditText.edittext.isFocusableInTouchMode = !isEditable
        customEditText.edittext.setOnClickListener {
            customEditText.iconRight.performClick()
        }
    }


}