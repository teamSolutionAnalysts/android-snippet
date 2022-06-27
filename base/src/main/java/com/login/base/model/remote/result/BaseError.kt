package com.login.base.model.remote.result

import com.google.gson.annotations.SerializedName

class BaseError : BaseResult() {
    @SerializedName("code")
    var code: String? = null

    @SerializedName(value = "message", alternate = ["error"])
    var message: String? = null

    @SerializedName("isMandatory")
    var isMandatory: Int? = null ?: -1
}