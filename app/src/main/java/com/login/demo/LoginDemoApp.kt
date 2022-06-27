package com.login.demo

import androidx.multidex.MultiDex
import com.login.base.base.BaseApp
import com.login.base.di.baseAppModule
import com.login.base.di.baseNetworkModule
import com.login.demo.di.networkModule
import com.login.demo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class LoginDemoApp : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initializeKoin()

    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@LoginDemoApp)
            modules(
                listOf(
                    baseAppModule,
                    baseNetworkModule,
                    viewModelModule,
                    networkModule
                )
            )
        }
    }
}