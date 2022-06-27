package com.login.demo.remote.repository

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.login.base.model.BaseRepository
import com.login.base.model.EnumLoading
import com.login.base.model.Resource
import com.login.base.utils.ApiConstant
import retrofit2.Response

open class AppRepository(resource: Resources) : BaseRepository(resource) {

    override suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        resultLiveData: MutableLiveData<Resource<T>>?
    ): Any? {
        val result: Resource<T> = safeApiResult(call)
        var data: Any? = null
        when (result) {
            is Resource.Success -> data = result.data
            is Resource.Error -> {
                when {
                    resultLiveData == null -> {
                        data = result.error
                    }
                    result.error.code?.equals(ApiConstant.UNAUTH_EXCEPTION_STATUS)!! -> {
                        resultLiveData.postValue(Resource.Loading(EnumLoading.LOG_OUT))
                        resultLiveData.postValue(Resource.Error(result.error))
                    }
                    else -> {
                        resultLiveData.postValue(Resource.Error(result.error))
                    }
                }
            }
        }
        return data
    }
}