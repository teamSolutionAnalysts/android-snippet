package com.login.base.model.local.preference

interface IPreferenceManager {

    fun isFirstTime(): Boolean

    fun saveAccessToken(accessToken: String?)

    fun getAccessToken(): String?

    fun isLoggedIn(): Boolean

    fun setLoggedIn(isLoggedIn: Boolean)

    fun clearAccessToken()

}