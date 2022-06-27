package com.login.demo.remote.repository

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.login.base.model.EnumLoading
import com.login.base.model.Resource
import com.login.base.model.local.preference.PreferenceManager
import com.login.base.utils.Constant
import com.login.demo.remote.IApiService
import com.login.demo.remote.request.*
import com.login.demo.remote.result.*
import okhttp3.MediaType
import okhttp3.RequestBody

class UserRepository constructor(
    private val apiService: IApiService,
    private val preferenceManager: PreferenceManager,
    resource: Resources
) : AppRepository(resource) {



    val signUpLiveData = MutableLiveData<Resource<ResSignUp>>()
    suspend fun requestSignUp(request: ReqSignUp) {
        signUpLiveData.postValue(Resource.Loading(EnumLoading.LOADING_ALL))

        val title = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.title.toString()
        )
        val firstname = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.firstname.toString()
        )
        val lastname = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.lastname.toString()
        )
        val email = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.email.toString()
        )
        val mobileNumber = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.mobileNumber.toString()
        )
        val countryCode = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.countryCode.toString()
        )

        val country = RequestBody.create(
            MediaType.parse(Constant.MEDIA_MIME_TEXT_PLAIN),
            request.country.toString()
        )

        val result = safeApiCall(
            call = {
                apiService.userSignUp(
                    title,
                    firstname,
                    lastname,
                    email,
                    mobileNumber,
                    countryCode,
                    country
                ).await()
            },
            resultLiveData = signUpLiveData
        )
        result?.let {
            if (result is ResSignUp) {
                saveToken(result.token)
                signUpLiveData.postValue(Resource.Success(result))
            }
        }
    }


    private fun saveToken(token: String) {
        preferenceManager.saveAccessToken(token)
    }

    val loginLiveData = MutableLiveData<Resource<ResSignIn>>()
    suspend fun requestSignIn(request: ReqSignIn) {
        loginLiveData.postValue(Resource.Loading(EnumLoading.LOADING_ALL))
        val result = safeApiCall(
            call = { apiService.userSignIn(request).await() },
            resultLiveData = loginLiveData
        )
        result?.let {
            if (result is ResSignIn) {
                saveToken(result.token)
                loginLiveData.postValue(Resource.Success(result))
            }
        }
    }


}
