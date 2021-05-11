package com.cj.base.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import com.cj.base.*
import com.cj.base.model.BaseModel
import com.cj.framework.event.SingleLiveEvent

/**
 * BaseViewModelMVVM
 *
 * @author CJ
 * @date 2021/5/8 11:12
 */
abstract class BaseViewModelMVVM<M : BaseModel>(application: Application) :
    AndroidViewModel(application),
    IBaseViewModel {

    abstract fun createModel(): M

    val mShowLoadingEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val mDismissLoadingEvent: SingleLiveEvent<Void> by lazy { SingleLiveEvent() }
    val mStartActivityEvent: SingleLiveEvent<Map<String, Any>> by lazy { SingleLiveEvent() }
    val mStartContainerActivityEvent: SingleLiveEvent<Map<String, Any>> by lazy { SingleLiveEvent() }
    val mFinishEvent: SingleLiveEvent<Void> by lazy { SingleLiveEvent() }

    private val mModel: M by lazy { createModel() }

    override fun onCleared() {
        super.onCleared()
        mModel.onCleared()
    }

    fun showLoadingDialog(title: String?) {
        mShowLoadingEvent.value = title
    }

    fun dismissLoadingDialog() {
        mDismissLoadingEvent.call()
    }

    fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[CLASS] = clazz
        bundle?.let {
            params[BUNDLE] = bundle
        }
        mStartActivityEvent.value = params
    }

    fun startContainerActivity(canonicalName: String) {
        startContainerActivity(canonicalName, null)
    }

    fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[FRAGMENT_CANONICAL_NAME] = canonicalName
        bundle?.let {
            params[BUNDLE] = bundle
        }
        mStartContainerActivityEvent.value = params
    }

    //生命周期感知---->start
    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }
    //生命周期感知---->end

}