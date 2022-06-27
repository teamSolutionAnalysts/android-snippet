package com.login.base.model

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.login.base.R
import com.login.base.model.remote.result.BaseError
import com.login.base.utils.ApiConstant
import com.login.base.utils.CommonUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.net.ConnectException


open class BaseRepository constructor(private val resource: Resources) {

    open suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        resultLiveData: MutableLiveData<Resource<T>>? = null
    ): Any? {
        val result: Resource<T> = safeApiResult(call)
        return result
    }

    suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                return if (response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    val baseError = BaseError()
                    baseError.code = ApiConstant.SOMETHING_WRONG_ERROR_STATUS
                    baseError.message = resource.getString(R.string.error_something_went_wrong)
                    Resource.Error(baseError)
                }
            } else {
                if (response.errorBody() != null) {

                    if (response.headers()[ApiConstant.HEADER_KEY_CONTENT_TYPE]?.contains(
                            ApiConstant.HEADER_VALUE_CONTENT_TYPE_JSON
                        )!!
                    ) { //check if response is in json format
                        //todo add when (response.code()) {}
                        val errorString = response.errorBody()?.string()
                        val baseError = BaseError()
                        if (CommonUtils.isNotNullOrNotEmpty(errorString)) {
                            val jsonResponse = JSONObject(errorString)
                            try {
                                baseError.code = response.code().toString()
                                val error = if (jsonResponse.has(ApiConstant.KEY_ERROR)) {
                                    jsonResponse.get(ApiConstant.KEY_ERROR).toString()
                                } else {
                                    jsonResponse.get(ApiConstant.KEY_MESSAGE).toString()
                                }
                                if (error.contains(ApiConstant.KEY_MESSAGE)) {
                                    val jsonError = JSONObject(error)
                                    baseError.message =
                                        jsonError.get(ApiConstant.KEY_MESSAGE).toString()
                                } else {
                                    baseError.message = error
                                }
                                if (response.code() == ApiConstant.NOT_APPLICABLE_CODE) {
                                    baseError.isMandatory =
                                        jsonResponse.get(ApiConstant.KEY_IS_MANDATORY) as Int
                                }
                            } catch (e: Exception) {
                                baseError.code = ApiConstant.SOMETHING_WRONG_ERROR_STATUS
                                baseError.message =
                                    resource.getString(R.string.error_something_went_wrong)
                            }
                        } else {
                            baseError.code = ApiConstant.SOMETHING_WRONG_ERROR_STATUS
                            baseError.message =
                                resource.getString(R.string.error_something_went_wrong)
                        }
                        return Resource.Error(baseError)

                    } else { //if response is not in json format than handle it

                        when (response.code()) {
                            401 -> {
                                val baseError = BaseError()
                                baseError.code = ApiConstant.UNAUTH_EXCEPTION_STATUS
                                baseError.message = resource.getString(R.string.error_unauthorized)
                                return Resource.Error(baseError)
                            }
                            403 -> {
                                val baseError = BaseError()
                                baseError.code = ApiConstant.IO_EXCEPTION_STATUS
                                baseError.message = resource.getString(R.string.error_io_exception)
                                return Resource.Error(baseError)
                            }
                            500 -> {
                                val baseError = BaseError()
                                baseError.code = ApiConstant.INTERNAL_SERVER_STATUS
                                baseError.message =
                                    resource.getString(R.string.error_internal_server)
                                return Resource.Error(baseError)
                            }
                            413 -> {
                                val baseError = BaseError()
                                baseError.code = ApiConstant.LARGEIMAGE_UPLOAD_FAIL_STATUS
                                baseError.message = response.message()
                                return Resource.Error(baseError)
                            }
                            else -> {
                                val baseError = BaseError()
                                baseError.code = ApiConstant.SOMETHING_WRONG_ERROR_STATUS
                                baseError.message =
                                    resource.getString(R.string.error_something_went_wrong)
                                return Resource.Error(baseError)
                            }
                        }
                    }
                } else {
                    val baseError = BaseError()
                    baseError.code = ApiConstant.SOMETHING_WRONG_ERROR_STATUS
                    baseError.message = resource.getString(R.string.error_something_went_wrong)
                    return Resource.Error(baseError)
                }

            }
        } catch (error: Exception) {
            return when (error) {
                is ConnectException -> {
                    val baseError = BaseError()
                    baseError.code = ApiConstant.TIME_OUT_CONNECTION_STATUS
                    baseError.message = resource.getString(R.string.error_connection_timeout)
                    Resource.Error(baseError)
                }

                else -> {
                    val baseError = BaseError()
                    baseError.code = ApiConstant.SOMETHING_WRONG_ERROR_STATUS
                    baseError.message = resource.getString(R.string.error_something_went_wrong)
                    Resource.Error(baseError)
                }
            }

        }

    }

    fun createRequestBody(file: File, mediaType: String): RequestBody {
        return RequestBody.create(MediaType.parse(mediaType), file)
    }
}