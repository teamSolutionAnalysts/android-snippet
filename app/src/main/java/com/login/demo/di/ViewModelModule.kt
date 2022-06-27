package com.login.demo.di

import com.login.demo.appview.authentication.login.viewmodel.LoginViewModel
import com.login.demo.appview.authentication.register.viewmodel.RegisterViewModel
import com.login.demo.remote.repository.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory {
        UserRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        LoginViewModel(get(), get())
    }

    viewModel {
        RegisterViewModel(get(), get(), get(), get())
    }


}