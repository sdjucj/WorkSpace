package com.cj.framework.loadsir

import com.cj.framework.R
import com.kingja.loadsir.callback.Callback

/**
 * TimeoutCallback
 *
 * @author CJ
 * @date 2021/5/10 10:28
 */
class TimeoutCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_timeout
    }
}