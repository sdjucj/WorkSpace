package com.cj.framework.utils

import android.os.Build
import androidx.annotation.ColorRes
import com.cj.framework.BaseApplication

/**
 * ResourcesUtils
 *
 * @author CJ
 * @date 2021/5/11 16:38
 */

fun getColor(@ColorRes colorId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        BaseApplication.sApplication.getColor(colorId)
    } else {
        BaseApplication.sApplication.resources.getColor(colorId)
    }
}