package com.cj.framework

/**
 * app编译信息
 *
 * @author CJ
 * @date 2021/5/11 9:29
 */
interface IAppBuildConfig {

    /**
     * @return 是否是debug模式
     */
    fun isDebug(): Boolean

    /**
     * @return 版本号
     */
    fun getVersionCode(): String

    /**
     * @return 版本名称
     */
    fun getVersionName(): String

    /**
     * @return 包名
     */
    fun getApplicationId(): String

    /**
     * @return app编译类型
     */
    fun getBuildType(): String

}