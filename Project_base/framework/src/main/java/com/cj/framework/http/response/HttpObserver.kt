package com.cj.framework.http.response

import com.cj.framework.http.exception.ResponseException
import io.reactivex.observers.DisposableObserver

/**
 * 网络请求观察者
 *
 * @author CJ
 * @date 2021/5/11 14:04
 */
open class HttpObserver<T>(private val callback: IResponseCallback<T>?) :
    DisposableObserver<HttpResponse<T>>() {

    override fun onNext(response: HttpResponse<T>) {
        callback?.run {
            if (response.isSucceed()) {
                onSucceed(response.getData())
            } else {
                onError(response.getMsg())
            }
        }
    }

    override fun onError(e: Throwable) {
        callback?.run {
            val msg: String? = if (e is ResponseException) {
                e.msg
            } else {
                e.message
            }
            onError(msg ?: "网络异常")
        }
    }

    override fun onComplete() {

    }

}