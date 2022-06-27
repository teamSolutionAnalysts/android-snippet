package com.login.base.model.local.preference

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.login.base.utils.Constant


class PreferenceManager constructor(var sharedPreferences: SharedPreferences) : IPreferenceManager {

    companion object {
        const val KEY_FIRST_TIME = "pre.key.app.firstTime"
        const val KEY_ACCESS_TOKEN = "pre.key.accessToken"
        const val KEY_DEVICE_ID = "pre.key.device_id"
        const val KEY_IS_LOGGED_IN = "pre.key.is_logged_in"

    }

    override fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_TIME, true)
    }


    override fun saveAccessToken(accessToken: String?) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    override fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "")
    }

    override fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    override fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    override fun clearAccessToken() {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply()
    }


    fun getDeviceId(): Int {
        return sharedPreferences.getInt(KEY_DEVICE_ID, 0)
    }



    fun setNetworkAvailable(isNetwork: Boolean) {
        sharedPreferences.edit().putBoolean(Constant.PREF_NETWORK_STATE, isNetwork).apply()
    }





    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(obj: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(obj)
        //Save that String in SharedPreferences
        sharedPreferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = sharedPreferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}