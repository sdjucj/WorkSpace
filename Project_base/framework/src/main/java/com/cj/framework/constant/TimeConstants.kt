package com.cj.framework.constant

import androidx.annotation.IntDef

/**
 * 时间常量
 *
 * @author CJ
 * @date 2021/4/26 17:31
 */
object TimeConstants {
    const val M_SEC = 1//毫秒
    const val SEC = M_SEC * 1000//秒
    const val MIN = SEC * 60//分
    const val HOUR = MIN * 60//小时
    const val DAY = HOUR * 24//天

    @IntDef(M_SEC, SEC, MIN, HOUR, DAY)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Unit

}