package com.login.demo.appview.authentication.login.viewmodel

import android.content.res.Resources
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.login.base.model.Resource
import com.login.base.model.local.preference.PreferenceManager
import com.login.demo.appview.authentication.login.models.LoginFormField
import com.login.demo.remote.repository.UserRepository
import com.login.demo.remote.request.ReqSignIn
import com.login.demo.utils.AppConstant
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch


class LoginViewModel(
    private val util: PhoneNumberUtil,
    private val userRepository: UserRepository,
) :
    ViewModel() {
    var notifyClick = MutableLiveData<Int>()
    val logInFields: LoginFormField = LoginFormField()
    var errMobile: ObservableField<String> = ObservableField("")
    var errPassword: ObservableField<String> = ObservableField("")
    var isPasswordVisible = MutableLiveData<Int>(0)
    var isMobileValid = true
    var isPasswordValid = true
    val isFromAuth: MutableLiveData<Boolean> = MutableLiveData()
    var countryCode: ObservableField<String> = ObservableField(AppConstant.DEFAULT_COUNTRY_CODE)
    var utilPhoneNumberUtil: PhoneNumberUtil? = null
    val resultLiveData = MutableLiveData<Resource<Any>>()
    private val resultObserver = Observer<Resource<Any>> {
        resultLiveData.postValue(it)
    }

    init {
        userRepository.loginLiveData.observeForever(resultObserver)
        utilPhoneNumberUtil=util
    }


    fun onClick(view: View) {
        notifyClick.postValue(view.id)
    }


    fun apiLogin() {
        viewModelScope.launch {
            val request = ReqSignIn(
                logInFields.mobile,
                logInFields.password,
                countryCode.get()
            )
            userRepository.requestSignIn(request)
        }
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.loginLiveData.postValue(null)
        userRepository.loginLiveData.removeObserver(resultObserver)

    }
}
