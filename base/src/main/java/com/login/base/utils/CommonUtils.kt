package com.login.base.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern



object CommonUtils {

    private val EMAIL_PATTERN: Pattern =
        Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")


    fun hideKeyboard(context: AppCompatActivity) {
        val view = context.currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun isNotNullOrNotEmpty(value: String?): Boolean {
        return value != null && value.trim() != ""
    }

    fun isValidEmail(email: String): Boolean {
        return EMAIL_PATTERN.matcher(email).matches()
    }



    fun isValidPhoneNumber(util: PhoneNumberUtil, phoneNumber: String): Boolean {
        if (phoneNumber.isNotEmpty()) {
            try {
                val phoneNum = util.parse(phoneNumber, "LB")
                val formattedNumber =
                    util.format(phoneNum, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                val verificationNumber = util.parse(formattedNumber, "LB")
                return util.isValidNumber(verificationNumber)
            } catch (e: NumberParseException) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }


    fun loadJSONFromAsset(activity: Activity, filename: String): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = activity.assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}
