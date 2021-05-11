package com.cj.framework.http.response

/**
 * HttpResponse
 *
 * @author CJ
 * @date 2021/5/11 10:41
 */
abstract class HttpResponse<T> {

    /**
     * @return true 请求成功
     */
    abstract fun isSucceed(): Boolean

    /**
     * @return 错误代码
     */
    abstract fun getErrorCode(): Int

    /**
     * @return 提示信息
     */
    abstract fun getMsg(): String?

    /**
     * @return 数据内容
     */
    abstract fun getData(): T?


}