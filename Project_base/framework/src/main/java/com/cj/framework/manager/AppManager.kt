package com.cj.framework.manager

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.Exception
import java.util.*

/**
 * AppManager
 *
 * @author CJ
 * @date 2021/5/7 9:56
 */
object AppManager {

    private val sActivityStack: Stack<Activity> = Stack()
    private val sFragmentStack: Stack<Fragment> = Stack()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        sActivityStack.add(activity)
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity) {
        sActivityStack.remove(activity)
    }

    /**
     * 是否有activity
     */
    fun hasActivity(): Boolean {
        return sActivityStack.isNotEmpty()
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return sActivityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        finishActivity(sActivityStack.lastElement())
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in sActivityStack) {
            if (activity?.javaClass == clazz) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        sActivityStack.forEach {
            it?.let {
                finishActivity(it)
            }
        }
    }

    /**
     * 获取指定的Activity
     */
    fun getActivity(clazz: Class<*>): Activity? {
        for (activity in sActivityStack) {
            if (activity?.javaClass == clazz) {
                return activity
            }
        }
        return null
    }

    /**
     * 添加Fragment到堆栈
     */
    fun addFragment(fragment: Fragment) {
        sFragmentStack.add(fragment)
    }

    /**
     * 移除指定的Fragment
     */
    fun removeFragment(fragment: Fragment) {
        sFragmentStack.remove(fragment)
    }

    /**
     * 是否有Fragment
     */
    fun hasFragment(): Boolean {
        return sFragmentStack.isNotEmpty()
    }

    /**
     * 获取当前Fragment（堆栈中最后一个压入的）
     */
    fun currentFragment(): Fragment {
        return sFragmentStack.lastElement()
    }

    /**
     * 退出应用程序
     */
    fun appExit() {
        try {
            finishActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sActivityStack.clear()
            sFragmentStack.clear()
        }
    }

}