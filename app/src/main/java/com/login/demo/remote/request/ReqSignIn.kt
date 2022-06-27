package com.login.demo.remote.request

import androidx.annotation.Keep

@Keep
data class ReqSignIn(
    val mobileNumber: String,
    val password: String,
    val countryCode: String?
)