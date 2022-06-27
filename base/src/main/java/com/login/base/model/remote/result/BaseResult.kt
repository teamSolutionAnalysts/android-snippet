package com.login.base.model.remote.result

open class BaseResult {
    var status: Boolean = false
    var error: Error? = null
}

data class Error(val code: String?, val message: String?)