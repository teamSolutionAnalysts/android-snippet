package com.login.base.utils

object ApiConstant {

    /*todo add url in different flavour*/
    const val BASE_URL = Environment.BASE_URL


    //    const val BASE_PREFIX = "api/"
    const val BASE_PREFIX = ""

    const val INTERCEPTOR_LOGGING = "LOGGING_INTERCEPTOR"
    const val INTERCEPTOR_OK_HTTP = "OK_HTTP_INTERCEPTOR"
    const val TAG_OK_HTTP = "OkHttp"

    const val INTERNAL_SERVER_STATUS = "500"

    const val NO_INTERNET_CONNECTION = "No internet connection."
    const val NO_INTERNET_CONNECTION_STATUS = "503"

    const val TIME_OUT_CONNECTION = "Cannot connect to server.\nPlease try again later."
    const val TIME_OUT_CONNECTION_STATUS = "504"

    const val SOMETHING_WRONG_ERROR = "Something went wrong!!\nPlease try again later."
    const val SOMETHING_WRONG_ERROR_STATUS = "505"
    const val IO_EXCEPTION_STATUS = "403"
    const val UNAUTH_EXCEPTION_STATUS = "401"
    const val NOT_APPLICABLE_CODE = 406

    const val LARGEIMAGE_UPLOAD_FAIL_STATUS = "413"

    const val HEADER_KEY_AUTHORIZATION: String = "authorization"
    const val HEADER_KEY_LOCALE: String = "X-L10N-Locale"
    const val HEADER_KEY_TOKEN: String = "token"
    const val HEADER_KEY_X_API_KEY: String = "x-api-key"

    const val HEADER_KEY_CONTENT_TYPE: String = "Content-Type"
    const val HEADER_VALUE_CONTENT_TYPE_JSON: String = "application/json"

    const val KEY_ERROR: String = "error"
    const val KEY_MESSAGE: String = "message"
    const val KEY_IS_MANDATORY: String = "isMandatory"




}