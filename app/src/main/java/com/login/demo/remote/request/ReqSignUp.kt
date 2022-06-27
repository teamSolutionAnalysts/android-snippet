package com.login.demo.remote.request

import androidx.annotation.Keep

@Keep
data class ReqSignUp(
    val title: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val mobileNumber: String,
    val countryCode: String,
    val country: String?,
)