package com.login.base.base

interface ConnectionBridge {

    fun isNetworkAvailable(): Boolean

    fun checkNetworkAvailableWithError(): Boolean
}