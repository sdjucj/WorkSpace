package com.cj.framework.http.exception

import java.lang.Exception

/**
 * 服务器异常
 *
 * @author CJ
 * @date 2021/5/11 10:48
 */
class ServerException(val code: Int?, val msg: String?) : Exception()