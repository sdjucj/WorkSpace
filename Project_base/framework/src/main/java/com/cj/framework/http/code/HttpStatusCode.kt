package com.cj.framework.http.code

import androidx.annotation.IntDef

/**
 * 网络状态代码
 *
 * @author CJ
 * @date 2021/5/11 11:09
 */
const val HTTP_STATUS_SUCCEED = 200//成功
const val HTTP_STATUS_UNAUTHORIZED = 401//未授权
const val HTTP_STATUS_FORBIDDEN = 403// 服务器拒绝请求
const val HTTP_STATUS_NOT_FOUND = 404//服务器找不到请求的网页
const val HTTP_STATUS_REQUEST_TIMEOUT = 408//服务器等候请求时发生超时
const val HTTP_STATUS_INTERNAL_SERVER_ERROR = 500//服务器内部错误
const val HTTP_STATUS_BAD_GATEWAY = 502//错误网关
const val HTTP_STATUS_SERVICE_UNAVAILABLE = 503// 服务器目前无法使用
const val HTTP_STATUS_GATEWAY_TIMEOUT = 504//网关超时

@IntDef(
    HTTP_STATUS_SUCCEED, HTTP_STATUS_UNAUTHORIZED, HTTP_STATUS_FORBIDDEN,
    HTTP_STATUS_NOT_FOUND, HTTP_STATUS_REQUEST_TIMEOUT, HTTP_STATUS_INTERNAL_SERVER_ERROR,
    HTTP_STATUS_BAD_GATEWAY, HTTP_STATUS_SERVICE_UNAVAILABLE, HTTP_STATUS_GATEWAY_TIMEOUT
)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpStatusCode(val value: Int = HTTP_STATUS_SUCCEED)