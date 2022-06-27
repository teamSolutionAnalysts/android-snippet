package com.login.demo.appview.authentication.register.models

import android.content.res.Resources
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.login.demo.BR
import com.login.demo.R

class RegisterFormFields(private val resources: Resources) : BaseObservable() {

    var _title: String = resources.getString(R.string.str_title)
    var _firstName: String = null ?: ""
    var _lastName: String = null ?: ""
    var _mobile: String = null ?: ""
    var _email: String = null ?: ""
    var _password: String = null ?: ""
    var _confirmPassword: String = null ?: ""


    var title: String
        @Bindable get() = _title
        set(value) {
            _title = value
            if (title != _title)
                notifyPropertyChanged(BR.title)

            notifyPropertyChanged(BR.allDataFilled)
        }

    var firstName: String
        @Bindable get() = _firstName
        set(value) {
            _firstName = value
            if (firstName != _firstName)
                notifyPropertyChanged(BR.firstName)

            notifyPropertyChanged(BR.allDataFilled)
        }

    var lastName: String
        @Bindable get() = _lastName
        set(value) {
            _lastName = value
            if (lastName != _lastName)
                notifyPropertyChanged(BR.lastName)

            notifyPropertyChanged(BR.allDataFilled)
        }

    var mobile: String
        @Bindable get() = _mobile
        set(value) {
            _mobile = value
            if (mobile != _mobile)
                notifyPropertyChanged(BR.mobile)

            notifyPropertyChanged(BR.allDataFilled)
        }

    var email: String
        @Bindable get() = _email
        set(value) {
            _email = value
            if (email != _email)
                notifyPropertyChanged(BR.email)

            notifyPropertyChanged(BR.allDataFilled)
        }

    var password: String
        @Bindable get() = _password
        set(value) {
            _password = value
            if (password != _password)
                notifyPropertyChanged(BR.password)

            notifyPropertyChanged(BR.allDataFilled)
        }

    var confirmPassword: String
        @Bindable get() = _confirmPassword
        set(value) {
            _confirmPassword = value
            if (confirmPassword != _confirmPassword)
                notifyPropertyChanged(BR.confirmPassword)

            notifyPropertyChanged(BR.allDataFilled)
        }




    var allDataFilled: Boolean = false
        @Bindable get() = !_title.equals(resources.getString(R.string.str_title)) &&
                _firstName.isNotEmpty() && _lastName.isNotEmpty() && _mobile.isNotEmpty() && _email.isNotEmpty()  && _password.isNotEmpty()  && _confirmPassword.isNotEmpty()
}