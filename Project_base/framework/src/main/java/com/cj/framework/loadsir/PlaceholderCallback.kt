package com.cj.framework.loadsir

import com.cj.framework.R
import com.kingja.loadsir.callback.Callback

/**
 * PlaceholderCallback
 *
 * @author CJ
 * @date 2021/5/10 10:42
 */
class PlaceholderCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_placeholder
    }
}