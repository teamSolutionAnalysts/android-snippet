package com.login.demo.remote.result

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.login.base.model.remote.result.BaseResult

@Keep
data class ResSignIn(
    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("mobileNumber")
    val mobileNumber: String,

    @field:SerializedName("isFirstLogin")
    val isFirstLogin: Boolean,

    @field:SerializedName("verified")
    val verified: Int

) : BaseResult()