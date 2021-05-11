package com.cj.framework.http.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

/**
 * GsonRequestBodyConverter
 *
 * @author CJ
 * @date 2021/5/10 18:34
 */
class GsonRequestBodyConverter<T>() : Converter<T, RequestBody> {

    companion object {
        private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
        private val UTF_8 = Charset.forName("UTF-8")
    }


    private var mGson: Gson? = null
    private lateinit var mAdapter: TypeAdapter<T>

    constructor(gson: Gson?, adapter: TypeAdapter<T>) : this() {
        this.mGson = gson
        this.mAdapter = adapter
    }


    @Throws(IOException::class)
    override fun convert(value: T): RequestBody? {
        val data: ByteArray? = when (value) {
            is JSONObject -> {
                value.toString().toByteArray()
            }
            is String -> {
                (value as String).toByteArray()
            }
            else -> {
                val buffer = Buffer()
                val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
                mGson?.let {
                    val jsonWriter = it.newJsonWriter(writer)
                    mAdapter.write(jsonWriter, value)
                    jsonWriter.close()
                    buffer.readByteArray()
                }
            }
        }
        return data?.toRequestBody(MEDIA_TYPE)
    }
}