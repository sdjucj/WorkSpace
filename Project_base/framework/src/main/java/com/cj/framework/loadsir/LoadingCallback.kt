package com.cj.framework.loadsir

import android.content.Context
import android.view.View
import android.widget.TextView
import com.cj.framework.R
import com.kingja.loadsir.callback.Callback

/**
 * LoadingCallback
 *
 * @author CJ
 * @date 2021/5/10 10:27
 */
class LoadingCallback() : Callback() {

    private var mTitle: String? = null
    private var mSubTitle: String? = null

    constructor(title: String?) : this() {
        this.mTitle = title
    }

    constructor(title: String?, subTitle: String?) : this() {
        this.mTitle = title
        this.mSubTitle = subTitle
    }

    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun onViewCreate(context: Context?, view: View?) {
        super.onViewCreate(context, view)
        view?.run {
            mTitle?.let {
                findViewById<TextView>(R.id.tv_title)?.text = it
            }
            mSubTitle?.let {
                findViewById<TextView>(R.id.tv_subTitle)?.text = it
            }
        }
    }
}