package com.cj.framework.loadsir

import com.cj.framework.R
import com.kingja.loadsir.callback.Callback

/**
 * ErrorCallback
 *
 * @author CJ
 * @date 2021/5/10 10:26
 */
class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}