package com.cj.framework.http.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * TODO
 * @author CJ
 * @date 2021/5/10 18:50
 */
class GsonResponseBodyConverter<T>() : Converter<ResponseBody, T> {
    private var mGson: Gson? = null
    private var mAdapter: TypeAdapter<T>? = null

    constructor(gson: Gson?, adapter: TypeAdapter<T>?) : this() {
        this.mGson = gson
        this.mAdapter = adapter
    }

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        return mGson?.let {
            mAdapter?.run {
                value.use { value ->
                    val jsonReader = it.newJsonReader(value.charStream())
                    val result = read(jsonReader)
                    if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                        throw JsonIOException("JSON document was not fully consumed.")
                    }
                    result
                }
            }
        }
    }
}