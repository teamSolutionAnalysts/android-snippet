package com.login.demo.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.login.base.utils.ApiConstant
import com.login.demo.remote.IApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    // Dependency: Retrofit
    single {
        Retrofit.Builder().baseUrl(ApiConstant.BASE_URL).client(get())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    // Dependency: ApiService
    single {
        val retrofit: Retrofit = get()
        retrofit.create(IApiService::class.java)
    }

}