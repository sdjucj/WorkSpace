package com.cj.framework.http

import com.cj.framework.BaseApplication
import com.cj.framework.BuildConfig
import com.cj.framework.http.code.*
import com.cj.framework.http.converter.JsonOrXmlConverterFactory
import com.cj.framework.http.exception.ResponseException
import com.cj.framework.http.exception.ServerException
import com.cj.framework.http.interceptor.RequestInterceptor
import com.cj.framework.http.interceptor.ResponseInterceptor
import com.cj.framework.http.response.HttpResponse
import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.ConnectException
import java.net.Proxy
import java.net.SocketTimeoutException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 * HttpClient
 * 网络请求
 *
 * @author CJ
 * @date 2021/5/10 16:56
 */
class HttpClient private constructor() {

    companion object {
        val sInstance = Holder.holder

        private const val CACHE_SIZE: Long = 100 * 1024 * 1024 // 10MB

        const val GLOBAL_DOMAIN = "Domain-Name"
        private const val API_CALENDAR = "calendar"
        const val GLOBAL_DOMAIN_CALENDAR: String = "$GLOBAL_DOMAIN:$API_CALENDAR"
    }

    private object Holder {
        val holder = HttpClient()
    }

    private val mBaseUrl: String = "http://8.130.166.196:19700"
    private var mOkHttpClient: OkHttpClient? = null
    private val mRetrofitHashMap: HashMap<String, Retrofit> = HashMap()

    /**
     * 获取网络请求服务接口
     */
    fun <T> getService(clazz: Class<T>): T {
        return getRetrofit(clazz).create(clazz)
    }

    /**
     * 设置线程调度
     */
    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(getAppErrorHandler())
                .onErrorResumeNext(Function<Throwable, Observable<T>> {
                    Observable.error(throwException(it))
                })
        }
    }

    private fun <T> getRetrofit(clazz: Class<T>): Retrofit {
        if (mRetrofitHashMap[mBaseUrl + clazz.name] != null) {
            return mRetrofitHashMap[mBaseUrl + clazz.name] as Retrofit
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(JsonOrXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        mRetrofitHashMap[mBaseUrl + clazz.name] = retrofit
        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient {
        return mOkHttpClient ?: createOkHttpClient()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(BaseApplication.sApplication.cacheDir, CACHE_SIZE))
        builder.addInterceptor(getInterceptor())
        builder.addInterceptor(RequestInterceptor())
        builder.addInterceptor(ResponseInterceptor())
        if (BaseApplication.sApplication.isDebug()) {
            //测试环境日志处理
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLoggingInterceptor)
        } else {
            //正式环境防止抓包处理
            builder.proxy(Proxy.NO_PROXY)
        }
        mOkHttpClient = builder.build()
        return mOkHttpClient ?: throw IllegalStateException("createOkHttpClient failed")
    }

    /**
     * 请求接口增加操作
     *
     * @return okHttp拦截器
     */
    private fun getInterceptor(): Interceptor {
        return Interceptor {
            //获取request
            var request = it.request()
            //从request中获取原有的HttpUrl实例oldHttpUrl
            val oldHttpUrl = request.url
            //获取request的创建者builder
            val builder = request.newBuilder()
            //从request中获取headers，通过给定的键url_name
            val headerValues = request.headers(GLOBAL_DOMAIN)
            if (headerValues.isNotEmpty()) {
                //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                builder.removeHeader(GLOBAL_DOMAIN)
                //匹配获得新的BaseUrl
                //var newBaseUrl: HttpUrl? = oldHttpUrl
                val httpUrlBuilder = oldHttpUrl.newBuilder()
                val newBaseUrl = updateUrl(headerValues[0], oldHttpUrl, httpUrlBuilder)

                request = newBaseUrl?.run {
                    //重建新的HttpUrl，修改需要修改的url部分
                    val newFullUrl: HttpUrl = httpUrlBuilder
                        .scheme(scheme) //更换网络协议
                        .host(host) //更换主机名
                        .port(port) //更换端口
                        //.removePathSegment(0)//移除第一个参数
                        .build()
                    //重建这个request，通过builder.url(newFullUrl).build()；
                    // 然后返回一个response至此结束修改
                    builder.url(newFullUrl).build()
                    //it.proceed(builder.url(newFullUrl).build())
                } ?: request
            }
            it.proceed(request)
        }
    }

    private fun <T> getAppErrorHandler(): Function<T, T> {
        return Function<T, T> { response: T ->
            when (response) {
                is HttpResponse<*> -> {
                    if (!response.isSucceed()) {
                        throw ServerException(response.getErrorCode(), response.getMsg())
                    }
                }
            }
            response
        }
    }

    private fun updateUrl(
        headerValue: String,
        oldHttpUrl: HttpUrl?,
        builder: HttpUrl.Builder,
    ): HttpUrl? {
        val newBaseUrl: HttpUrl?
        when (headerValue) {
            API_CALENDAR -> {
                //改变base url
                //newBaseUrl = "new base url".toHttpUrlOrNull()
                newBaseUrl = oldHttpUrl
                builder.addQueryParameter("busiType", "juheCalendar")
            }
            else -> {
                newBaseUrl = oldHttpUrl
            }
        }
        return newBaseUrl
    }

    private fun throwException(throwable: Throwable): ResponseException {
        val code: Int?
        var msg: String? = null

        when (throwable) {
            is HttpException -> {
                code = HTTP_ERROR_POLICY_ERROR
                if (throwable.code() == HTTP_STATUS_UNAUTHORIZED
                    || throwable.code() == HTTP_STATUS_FORBIDDEN
                    || throwable.code() == HTTP_STATUS_NOT_FOUND
                    || throwable.code() == HTTP_STATUS_REQUEST_TIMEOUT
                    || throwable.code() == HTTP_STATUS_INTERNAL_SERVER_ERROR
                    || throwable.code() == HTTP_STATUS_BAD_GATEWAY
                    || throwable.code() == HTTP_STATUS_SERVICE_UNAVAILABLE
                    || throwable.code() == HTTP_STATUS_GATEWAY_TIMEOUT
                ) {
                    msg = "网络错误"
                }
            }
            is ServerException -> {
                code = HTTP_ERROR_SERVER_ERROR
                msg = throwable.msg ?: "获取数据失败"
            }
            is JsonParseException,
            is JSONException,
            is ParseException,
            -> {
                code = HTTP_ERROR_PARSE_ERROR
                msg = "解析错误"
            }
            is ConnectException -> {
                code = HTTP_ERROR_NETWORK_ERROR
                msg = "网络连接失败"
            }
            is SSLHandshakeException -> {
                code = HTTP_ERROR_SSL_ERROR
                msg = "证书验证失败"
            }
            is ConnectTimeoutException,
            is SocketTimeoutException,
            -> {
                code = HTTP_ERROR_TIMEOUT_ERROR
                msg = "连接超时"
            }
            else -> {
                code = HTTP_ERROR_UNKNOWN
                msg = "未知错误"
            }
        }
        return ResponseException(throwable, code, msg)
    }


}