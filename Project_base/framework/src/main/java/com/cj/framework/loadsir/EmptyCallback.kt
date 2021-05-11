package com.cj.framework.loadsir

import com.cj.framework.R
import com.kingja.loadsir.callback.Callback

/**
 * EmptyCallback
 *
 * @author CJ
 * @date 2021/5/10 10:20
 */
class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}