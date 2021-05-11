package com.cj.base.activity

import android.R
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity


/**
 * BaseActivity
 *
 * @author CJ
 * @date 2021/5/6 19:56
 */
abstract class BaseActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        initContentView(getLayoutId())
        initData(savedInstanceState)
        initView()
        initListener()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_UP == event?.action) {
            currentFocus?.let {
                if (isSoftShowing() && isShouldHideInput(it, event)) {
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                }
            }

        }
        return super.dispatchTouchEvent(event)
    }

    protected open fun initContentView(@LayoutRes layoutId: Int) {
        setContentView(layoutId)
    }

    /**
     * 获取Activity的根布局
     */
    protected fun getRootView(): View? {
        return findViewById<ViewGroup>(R.id.content).getChildAt(0)
    }

    //软键盘是否显示
    private fun isSoftShowing(): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight = window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)

        return screenHeight - rect.bottom != 0
    }

    //是否需要隐藏键盘
    private fun isShouldHideInput(view: View, event: MotionEvent): Boolean {
        if (view is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + view.getHeight()
            val right = left + view.getWidth()
            // 点击的是输入框区域，保留点击EditText的事件
            return (event.x <= left || event.x >= right
                    || event.y <= top || event.y >= bottom)
        }
        return false
    }

}