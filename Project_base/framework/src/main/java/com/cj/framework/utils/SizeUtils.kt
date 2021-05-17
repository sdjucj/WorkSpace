package com.cj.framework.utils

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * SizeUtils
 *
 * @author CJ
 * @date 2021/5/11 17:51
 */
/**
 * Value of dp to value of px.
 */
fun dp2px(dp: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * Value of px to value of dp.
 */
fun px2dp(px: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * Value of sp to value of px.
 */
fun sp2px(sp: Float): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (sp * fontScale + 0.5f).toInt()
}

/**
 * Value of sp to value of px.
 */
fun px2sp(px: Float): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (px / fontScale + 0.5f).toInt()
}

/**
 * Value of screen width of px
 */
fun getScreenWidth(): Int {
    val dm: DisplayMetrics = Resources.getSystem().displayMetrics
    return dm.widthPixels
}