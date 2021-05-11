package com.cj.project.app

import com.cj.framework.BaseApplication
import com.cj.project.BuildConfig

/**
 * TODO
 * @author CJ
 * @date 2021/5/11 14:33
 */
class App:BaseApplication() {
    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun getVersionCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    override fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getApplicationId(): String {
        return BuildConfig.APPLICATION_ID
    }

    override fun getBuildType(): String {
        return BuildConfig.BUILD_TYPE
    }
}