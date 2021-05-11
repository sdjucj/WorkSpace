package com.cj.framework.http.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.lang.reflect.Type

/**
 * retrofit请求响应解析（xml、json）
 *
 * @author CJ
 * @date 2021/5/10 17:54
 */
class JsonOrXmlConverterFactory private constructor() : Converter.Factory() {

    private val mXmlFactory: Converter.Factory = SimpleXmlConverterFactory.create()
    private val mJsonFactory: Converter.Factory = GsonConverterFactory.create()

    companion object {
        fun create(): JsonOrXmlConverterFactory {
            return JsonOrXmlConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody?, *>? {
        annotations.forEach {
            when (it) {
                is ResponseFormat -> {
                    val value = it.value
                    if (XML == value) {
                        return mXmlFactory.responseBodyConverter(type, annotations, retrofit)
                    }
                }
            }
        }
        return mJsonFactory.responseBodyConverter(type, annotations, retrofit)
    }

}