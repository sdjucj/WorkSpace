package com.cj.framework.http.exception

import java.lang.Exception

/**
 * 响应异常
 *
 * @author CJ
 * @date 2021/5/11 10:44
 */
class ResponseException(val throwable: Throwable?, val code: Int?, val msg: String?) : Exception()