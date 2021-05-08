package com.cj.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.cj.framework.utils.getField

/**
 * BaseFragment
 *
 * @author CJ
 * @date 2021/5/6 19:57
 */
abstract class BaseFragment : Fragment() {

    /**
     * 获取布局ID
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData(bundle: Bundle?)

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 设置监听
     */
    abstract fun initListener()

    private val mBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(mBackPressedCallback)
        initData(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflateLayout(inflater, container, getLayoutId())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBackPressedCallback.remove()
    }

    protected fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?, @LayoutRes layoutId: Int
    ): View {
        return inflater.inflate(layoutId, container, false)
    }

    /**
     * 设置back按键处理事件
     */
    protected open fun setBackEnabled(enabled: Boolean) {
        mBackPressedCallback.isEnabled = enabled
    }

    /**
     * 返回键点击处理
     */
    protected fun onBackPressed() {
        //do nothing 子类重写
    }

    /**
     * 返回
     */
    protected fun back() {
        getField<Runnable>(
            requireActivity().onBackPressedDispatcher,
            "mFallbackOnBackPressed"
        )?.run()
    }

}