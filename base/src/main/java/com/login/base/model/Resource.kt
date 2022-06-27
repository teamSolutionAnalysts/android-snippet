package com.login.base.model

import com.login.base.model.remote.result.BaseError

sealed class Resource<out T> {
    class Success<out T>(val data: T) : Resource<T>()
    class Error<out T>(val error: BaseError) : Resource<T>()
    class Loading<out T>(val loading : EnumLoading) : Resource<T>()
}