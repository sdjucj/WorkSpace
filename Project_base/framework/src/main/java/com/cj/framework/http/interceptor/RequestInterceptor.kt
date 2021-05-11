package com.cj.framework.http.interceptor

import com.cj.framework.BaseApplication
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络请求头部拦截器
 *
 * @author CJ
 * @date 2021/5/10 20:00
 */
class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        //builder.cacheControl(CacheControl.FORCE_CACHE);
        builder.addHeader("os", "android")
        builder.addHeader("appVersion", BaseApplication.sApplication.getVersionCode())
        return chain.proceed(builder.build())
    }
}