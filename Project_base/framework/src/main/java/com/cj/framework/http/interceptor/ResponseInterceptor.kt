package com.cj.framework.http.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络请求响应体拦截器
 *
 * @author CJ
 * @date 2021/5/10 20:00
 */
class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        Log.d("ResponseInterceptor", "requestTime=" + (System.currentTimeMillis() - requestTime))
        return response
    }
}