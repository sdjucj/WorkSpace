package com.cj.framework.http.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance}
 * last to allow the other converters a chance to see their types.
 *
 * @author CJ
 * @date 2021/5/10 17:55
 */
class GsonConverterFactory private constructor() : Converter.Factory() {

    companion object {
        /**
         * Create an instance using a default [Gson] instance for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        fun create(): GsonConverterFactory {
            return create(Gson())
        }

        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        fun create(gson: Gson): GsonConverterFactory {
            return GsonConverterFactory(gson)
        }
    }


    private var mGson: Gson? = null

    private constructor(gson: Gson) : this() {
        this.mGson = gson
    }

    override fun responseBodyConverter(
        type: Type?, annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *>? {
        return mGson?.run {
            GsonResponseBodyConverter(this, getAdapter(TypeToken.get(type)))
        }
    }

    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody>? {
        return mGson?.run {
            GsonRequestBodyConverter(this, getAdapter(TypeToken.get(type)))
        }
    }
}