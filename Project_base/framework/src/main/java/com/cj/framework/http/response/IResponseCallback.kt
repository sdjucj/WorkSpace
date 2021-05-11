package com.cj.framework.http.response

/**
 * 网络请求结果回调接口
 *
 * @author CJ
 * @date 2021/5/11 14:06
 */
interface IResponseCallback<T> {
    /**
     * 请求成功
     *
     * @param result 返回数据
     */
    fun onSucceed(result: T?)

    /**
     * 请求失败
     *
     * @param message 错误信息
     */
    fun onError(message: String?)
}