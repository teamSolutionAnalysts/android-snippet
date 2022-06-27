package com.login.demo.remote

import androidx.annotation.Keep
import com.login.demo.remote.request.*
import com.login.demo.remote.result.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

@Keep
interface IApiService {

    @Multipart
    @POST("auth/sign-up/")
    fun userSignUp(
        @Part("title") title: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobileNumber") mobileNumber: RequestBody,
        @Part("countryCode") countryCode: RequestBody,
        @Part("country") country: RequestBody,
    ): Deferred<Response<ResSignUp>>


    @POST("auth/sign-in")
    fun userSignIn(@Body request: ReqSignIn): Deferred<Response<ResSignIn>>


}