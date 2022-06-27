package com.login.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log

object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager = context
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        return connectivityManager.activeNetworkInfo != null && connectivityManager
//            .activeNetworkInfo.isConnectedOrConnecting


        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("update_status", "TRANSPORT_CELLULAR Network is available : true")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("update_status", "TRANSPORT_WIFI Network is available : true")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("update_status", "TRANSPORT_ETHERNET Network is available : true")
                        return true
                    }
                }
            }
        } else {

            try {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    Log.i("update_status", "activeNetworkInfo Network is available : true")
                    return true
                }
            } catch (e: Exception) {
                Log.i("update_status", "" + e.message)
            }
        }
        Log.i("update_status", "Network is available : FALSE ")
        return false

    }
}