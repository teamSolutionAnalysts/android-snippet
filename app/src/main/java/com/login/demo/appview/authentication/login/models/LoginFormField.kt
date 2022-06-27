package com.login.demo.appview.authentication.login.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.login.demo.BR

class LoginFormField :
    BaseObservable() {
    private var _mobile: String = ""
    private var _password: String = ""

    var mobile: String
        @Bindable get() = _mobile
        set(value) {
            _mobile = value
            if (mobile != _mobile) {
                notifyPropertyChanged(BR.mobile)
            }
            notifyPropertyChanged(BR.allDataFilled)
        }

    var password: String
        @Bindable get() = _password
        set(value) {
            _password = value
            if (password != _password) {
                notifyPropertyChanged(BR.password)
            }
            notifyPropertyChanged(BR.allDataFilled)
        }

    val allDataFilled: Boolean
        @Bindable get() = _password.isNotEmpty() && _mobile.isNotEmpty()

    fun resetData() {
        mobile = ""
        password = ""
        notifyChange()
    }
}