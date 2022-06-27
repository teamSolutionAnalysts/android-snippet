package com.login.demo.appview.authentication.register.viewmodel

import android.content.res.Resources
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.login.base.model.Resource
import com.login.base.model.local.preference.PreferenceManager
import com.login.demo.appview.authentication.register.models.RegisterFormFields
import com.login.demo.remote.repository.UserRepository
import com.login.demo.remote.request.ReqSignUp
import com.login.demo.utils.AppConstant
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val util: PhoneNumberUtil,
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager,
    private val resources: Resources
) :
    ViewModel() {

    var notifyClick = MutableLiveData<Int>()
    val formField: RegisterFormFields = RegisterFormFields(resources)
    var errTitle: ObservableField<String> = ObservableField("")
    var errFirstName: ObservableField<String> = ObservableField("")
    var errLastName: ObservableField<String> = ObservableField("")
    var errMobile: ObservableField<String> = ObservableField("")
    var errEmail: ObservableField<String> = ObservableField("")
    var errPassword: ObservableField<String> = ObservableField("")
    var errConfirmPassword: ObservableField<String> = ObservableField("")
    var isPasswordVisible = MutableLiveData<Int>(0)
    var utilPhoneNumberUtil:PhoneNumberUtil?=null
    var isTitleValid = true
    var isFirsNameValid = true
    var isLastNameValid = true
    var isMobileValid = true
    var engTitle= ""
    var isEmailValid = true
    var isPasswordValid = true
    var isConfirmPassValid = true
    var countryCode: ObservableField<String> = ObservableField(AppConstant.DEFAULT_COUNTRY_CODE)
    val resultLiveData = MutableLiveData<Resource<Any>>()
    private val resultObserver = Observer<Resource<Any>> {
        resultLiveData.postValue(it)
    }


    init {
        userRepository.signUpLiveData.observeForever(resultObserver)
        utilPhoneNumberUtil=util
    }

    fun onClick(view: View) {
        notifyClick.postValue(view.id)
    }



    fun apiSignUp() {
        viewModelScope.launch {
            val request = ReqSignUp(
                formField.title,
                formField.firstName,
                formField.lastName,
                formField.email,
                formField.mobile,
                countryCode.get()!!,
                resources.configuration?.locales?.get(0)?.country,
            )
            userRepository.requestSignUp(request)
        }
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.signUpLiveData.postValue(null)
        userRepository.signUpLiveData.removeObserver(resultObserver)
    }
}
