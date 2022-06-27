package com.login.demo.customview

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.login.demo.R
import kotlinx.android.synthetic.main.custom_edittext.view.*


class CustomEditText : LinearLayout {
    var rootView: ViewGroup? = null

    constructor(context: Context?) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        rootView = View.inflate(context, R.layout.custom_edittext, null) as ViewGroup
        addView(rootView)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0)
        try {

            configureComponents(ta)

            reConfigureStartView(ta)
            configMargin(ta)

            txtSpinner.text = ta.getString(R.styleable.CustomEditText_spinnerText)

            setEdittextConfiguration(ta)

            val length = ta.getInt(R.styleable.CustomEditText_length, 0)
            if (length != 0) {
                edittext.filters = arrayOf(InputFilter.LengthFilter(length))
            }

            configureEdittextInputType(ta)

        } finally {
            ta.recycle()
        }
    }

    private fun configMargin(ta: TypedArray) {
        val required = ta.getBoolean(R.styleable.CustomEditText_required, false)
        if (required) {
            val params =
                FrameLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )

            params.setMargins(resources.getDimensionPixelSize(R.dimen._15sdp), 10, 0, 10)
            iconStar.layoutParams = params
        }
    }




    private fun setEdittextConfiguration(ta: TypedArray) {
        edittext.isEnabled = ta.getBoolean(R.styleable.CustomEditText_edittextEnabled, true)
        val hint = ta.getString(R.styleable.CustomEditText_hintEdittext)
        edittext.hint = hint

        edittext.setTextColor(
            ta.getInt(
                R.styleable.CustomEditText_colorEditTextColor,
                R.color.black
            )
        )

        /*viewline.setBackgroundColor(
            ta.getInt(
                R.styleable.CustomEditText_colorlinecolor,
                R.color.black
            )
        )*/

        edittext.setHintTextColor(
            ta.getInt(
                R.styleable.CustomEditText_colorHintEditTextColor,
                R.color.black
            )
        )


        val focusDisabled = ta.getBoolean(R.styleable.CustomEditText_focusDisabled, false)
        if (focusDisabled) {
            edittext.isFocusable = !focusDisabled
            edittext.isFocusableInTouchMode = !focusDisabled
            edittext.isCursorVisible = !focusDisabled
        }
    }

    private fun reConfigureStartView(ta: TypedArray) {
        val required = ta.getBoolean(R.styleable.CustomEditText_required, false)
        if (required) {
            iconStar.visibility = View.VISIBLE
        }

        if (iconLeft.visibility == View.GONE && !required) {
            framLeftIconGroup.visibility = View.GONE
        }

        if (iconLeft.visibility == View.GONE && required) {
            framLeftIconGroup.visibility = View.GONE
            iconStar2.visibility = View.VISIBLE
        }
    }

    /**
     * Configure all the components here
     */
    private fun configureComponents(ta: TypedArray) {
        ta.getString(R.styleable.CustomEditText_leftIconVisibility)?.let {
            configureIconsAndVisibility(
                iconLeft,
                it,
                VISIBILITY
            )
        }

        ta.getString(R.styleable.CustomEditText_rightIconVisibility)?.let {
            configureIconsAndVisibility(
                iconRight,
                it,
                VISIBILITY
            )
        }


        ta.getString(R.styleable.CustomEditText_spinnerVisibility)?.let {
            configureIconsAndVisibility(
                txtSpinner,
                it,
                VISIBILITY
            )
        }

        configureIconsAndVisibility(
            iconLeft,
            ta.getResourceId(R.styleable.CustomEditText_leftIcon, 0),
            RESOURCE
        )

        configureIconsAndVisibility(
            iconRight,
            ta.getResourceId(R.styleable.CustomEditText_rightIcon, 0),
            RESOURCE
        )
    }

    private fun configureEdittextInputType(ta: TypedArray) {
        val inputType = ta.getInt(R.styleable.CustomEditText_inputType, 0x00000001)
        //check for textpassword (Due to font changes in edittext)
        if (inputType == 0x00000081) {
            edittext.isSingleLine = true
            edittext.maxLines = 1
            edittext.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            edittext.inputType = inputType
        }
    }

    /**
     * Configure component's visibility and icons
     */
    private fun configureIconsAndVisibility(
        view: View,
        attrs: Any,
        attrType: String
    ) {
        when (attrType) {
            VISIBILITY -> {
                if (attrs == "1") {
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }

            RESOURCE -> {
                if ((view as AppCompatImageView).visibility == View.VISIBLE) {
                    view.setImageResource(attrs as Int)
                }
            }
        }

    }

    companion object {
        const val VISIBILITY = "visibility"
        const val RESOURCE = "resource"
    }

}