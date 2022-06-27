package com.login.base.base

import android.app.Application
import com.login.base.BuildConfig
import timber.log.Timber


open class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeTimber()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        if (instance != null) {
            instance = null
        }
    }

    companion object {
        var instance: BaseApp? = null
    }

}