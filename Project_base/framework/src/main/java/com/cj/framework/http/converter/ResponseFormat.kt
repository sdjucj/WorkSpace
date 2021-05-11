package com.cj.framework.http.converter

import androidx.annotation.StringDef

/**
 * 接口返回的数据格式，当前限定取值：{@link #JSON}或{@link #XML}
 *
 * @author CJ
 * @date 2021/5/10 19:28
 */
const val JSON = "json"
const val XML = "xml"

@StringDef(JSON, XML)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ResponseFormat(val value: String = JSON)
