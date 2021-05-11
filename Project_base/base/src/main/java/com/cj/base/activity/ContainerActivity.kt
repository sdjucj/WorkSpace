package com.cj.base.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.cj.base.BUNDLE
import com.cj.base.FRAGMENT_CANONICAL_NAME
import com.cj.base.R
import com.cj.framework.utils.forName
import com.cj.framework.utils.newInstance
import java.lang.ref.WeakReference

/**
 * ContainerActivity
 *
 * 盛装Fragment的一个容器(代理)Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 *
 * @author CJ
 * @date 2021/5/8 16:03
 */
open class ContainerActivity : BaseActivity() {

    companion object {
        private const val FRAGMENT_TAG = "content_fragment_tag"
    }

    protected lateinit var mFragment: WeakReference<Fragment>

    override fun getLayoutId(): Int {
        return R.layout.activity_container
    }

    override fun initData(bundle: Bundle?) {
        val fragmentManager = supportFragmentManager
        var fragment: Fragment? = null

        bundle?.run {
            fragment = fragmentManager.getFragment(this, FRAGMENT_TAG)
        }

        fragment = fragment ?: initFromIntent(intent)
        fragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content, it)
            transaction.commitAllowingStateLoss()
            mFragment = WeakReference(fragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mFragment.get()?.run {
            supportFragmentManager.putFragment(
                outState, FRAGMENT_TAG,
                this
            )
        }
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    protected open fun initFromIntent(intent: Intent?): Fragment {
        intent?.run {
            try {
                return getStringExtra(FRAGMENT_CANONICAL_NAME)?.let {
                    val fragment = newInstance<Fragment>(it)
                    val args = getBundleExtra(BUNDLE)
                    args?.run {
                        fragment.arguments = this
                    }
                    fragment
                } ?: throw IllegalStateException("can not find page fragmentName")
            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("fragment initialization failed!")
            }
        } ?: throw RuntimeException(
            "you must provide a page info to display"
        )
    }
}