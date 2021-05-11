package com.cj.base.model

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * BaseModel
 *
 * @author CJ
 * @date 2021/5/8 11:13
 */
open class BaseModel : Consumer<Disposable> {

    //解决RxJava内存泄漏
    private val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun accept(disposable: Disposable?) {
        addSubscribe(disposable)
    }

    /**
     * 清除
     */
    fun onCleared() {
        //取消所有异步任务
        mCompositeDisposable.clear()
    }

    private fun addSubscribe(disposable: Disposable?) {
        disposable?.let {
            mCompositeDisposable.add(disposable)
        }
    }
}