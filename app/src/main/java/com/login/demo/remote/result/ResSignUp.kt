package com.login.demo.remote.result

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.login.base.model.remote.result.BaseResult

@Keep
data class ResSignUp(
    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("countryCode")
    val countryCode: String,

    @field:SerializedName("mobileNumber")
    val mobileNumber: String,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("isVerified")
    val isVerified: Int

) : BaseResult()