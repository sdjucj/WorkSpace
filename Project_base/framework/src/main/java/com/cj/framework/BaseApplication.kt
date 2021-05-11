package com.cj.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.cj.framework.loadsir.*
import com.cj.framework.manager.AppManager
import com.kingja.loadsir.core.LoadSir

/**
 * CustomApplication
 *
 * @author CJ
 * @date 2021/5/6 19:59
 */
@SuppressLint("StaticFieldLeak")
abstract class BaseApplication : Application(), IAppBuildConfig {

    companion object {
        lateinit var sApplication: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        sApplication = this
        listenerActivityLifecycle(this)
        //加载反馈页管理初始化
        LoadSir.beginBuilder().addCallback(EmptyCallback())
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .addCallback(PlaceholderCallback())
            .addCallback(TimeoutCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

    private fun listenerActivityLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                AppManager.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.removeActivity(activity)
            }

        })
    }
}