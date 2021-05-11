package com.cj.framework.http.code

import androidx.annotation.IntDef

/**
 * 网络请求约定异常代码
 *
 * @author CJ
 * @date 2021/5/11 10:59
 */

const val HTTP_ERROR_UNKNOWN = 1000 //未知错误
const val HTTP_ERROR_PARSE_ERROR = 1001//解析错误
const val HTTP_ERROR_NETWORK_ERROR = 1002//网络错误
const val HTTP_ERROR_POLICY_ERROR = 1003//协议出错
const val HTTP_ERROR_SSL_ERROR = 1005//证书出错
const val HTTP_ERROR_TIMEOUT_ERROR = 1006//连接超时
const val HTTP_ERROR_SERVER_ERROR = 1007//服务器错误

@IntDef(
    HTTP_ERROR_UNKNOWN, HTTP_ERROR_PARSE_ERROR, HTTP_ERROR_NETWORK_ERROR,
    HTTP_ERROR_POLICY_ERROR, HTTP_ERROR_SSL_ERROR, HTTP_ERROR_TIMEOUT_ERROR,HTTP_ERROR_SERVER_ERROR
)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpErrorCode(val value: Int = HTTP_ERROR_UNKNOWN)
