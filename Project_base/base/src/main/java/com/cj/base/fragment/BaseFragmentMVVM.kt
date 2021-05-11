package com.cj.base.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.cj.base.*
import com.cj.base.activity.ContainerActivity
import com.cj.base.viewmodel.BaseViewModelMVVM
import com.cj.framework.loadsir.*
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import java.lang.IllegalStateException
import java.lang.reflect.ParameterizedType

/**
 * BaseFragmentMVVM
 *
 * @author CJ
 * @date 2021/5/8 11:09
 */
abstract class BaseFragmentMVVM<V : ViewDataBinding, VM : BaseViewModelMVVM<*>> : BaseFragment() {

    companion object {
        private const val TAG = "BaseFragmentMVVM"
    }

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM
    private var mRootLoadService: LoadService<*>? = null

    /**
     * 获取ViewModel ID
     */
    protected abstract fun getViewModelId(): Int

    /**
     * 初始化界面观察者的监听
     */
    protected abstract fun initViewObservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewDataBinding()
        initViewObservable()
        registerSingleLiveEvent()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //解除ViewModel生命周期感应
        lifecycle.removeObserver(mViewModel)
        mBinding.unbind()
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layoutId: Int
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.root
    }

    /**
     * @return viewModel实例
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel(): VM {
        //如果没有指定泛型参数，则默认使用BaseViewModel
        var modelClass: Class<VM>? = null
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            modelClass = type.actualTypeArguments[1] as Class<VM>
        }
        return ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(modelClass ?: BaseViewModelMVVM::class.java as Class<VM>)
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        mBinding.lifecycleOwner = this
        //关联ViewModel
        mBinding.setVariable(getViewModelId(), mViewModel)
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(mViewModel)
    }

    private fun registerSingleLiveEvent() {
        mViewModel.mShowLoadingEvent.observe(this, {
            showLoading(it)
        })
        mViewModel.mDismissLoadingEvent.observe(this, {
            dismissLoading()
        })
        mViewModel.mStartActivityEvent.observe(this, {
            startActivity(it)
        })
        mViewModel.mStartContainerActivityEvent.observe(this, {
            startContainerActivity(it)
        })
        mViewModel.mFinishEvent.observe(this, {
            requireActivity().finish()
        })
    }

    protected open fun showLoading(title: String?) {
        val loadSir = LoadSir.Builder()
            .addCallback(LoadingCallback(title))
            .setDefaultCallback(LoadingCallback::class.java)
            .build()
        mRootLoadService = view?.let { loadSir.register(it) }
    }

    protected open fun dismissLoading() {
        mRootLoadService?.showSuccess()
    }

    protected open fun startActivity(params: Map<String, Any>?) {
        params?.run {
            try {
                val clazz = this[CLASS]
                val bundle = this[BUNDLE]
                bundle?.run {
                    startActivity(clazz as Class<*>, this as Bundle)
                } ?: startActivity(clazz as Class<*>, null)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "startActivity ERROR : ${e.message}")
            }
        } ?: throw IllegalStateException("startActivity failed, params is null")
    }

    protected open fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    protected open fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(requireActivity(), clazz)
        bundle?.run {
            intent.putExtras(this)
        }
        startActivity(intent)
    }

    protected open fun startContainerActivity(params: Map<String, Any>?) {
        params?.run {
            try {
                val canonicalName = this[FRAGMENT_CANONICAL_NAME]
                val bundle = this[BUNDLE]
                bundle?.run {
                    startContainerActivity(canonicalName as String, bundle as Bundle)
                } ?: startContainerActivity(canonicalName as String, null)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "startActivity ERROR : ${e.message}")
            }
        } ?: throw IllegalStateException("startContainerActivity failed, params is null")
    }

    protected open fun startContainerActivity(canonicalName: String) {
        startContainerActivity(canonicalName, null)
    }

    protected open fun startContainerActivity(canonicalName: String, bundle: Bundle?) {
        val intent = Intent(requireActivity(), ContainerActivity::class.java)
        intent.putExtra(FRAGMENT_CANONICAL_NAME, canonicalName)
        bundle?.run {
            intent.putExtras(this)
        }
        startActivity(intent)
    }
}
