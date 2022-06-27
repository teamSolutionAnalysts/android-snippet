package com.login.base.di

import com.login.base.model.local.preference.PreferenceManager
import com.login.base.utils.ApiConstant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

val baseNetworkModule = module {

    // Dependency: HttpLoggingInterceptor
    single<Interceptor>(named(ApiConstant.INTERCEPTOR_LOGGING)) {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Timber.tag(ApiConstant.TAG_OK_HTTP).d(message)
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single(named(ApiConstant.INTERCEPTOR_OK_HTTP)) {
        Interceptor { chain ->
            val prefManger: PreferenceManager = get()
            val token = if (prefManger.getAccessToken() == null) "" else prefManger.getAccessToken()
            chain.proceed(
                chain.request().newBuilder().addHeader(ApiConstant.HEADER_KEY_AUTHORIZATION, token)
                    .build()
            )
        }
    }

    // Dependency: OkHttpClient
    single {
        OkHttpClient.Builder().connectTimeout(240, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS).writeTimeout(240, TimeUnit.SECONDS)
            .addInterceptor(get<Interceptor>(named(ApiConstant.INTERCEPTOR_LOGGING)))
            .addInterceptor(get<Interceptor>(named(ApiConstant.INTERCEPTOR_OK_HTTP))).build()
    }


}