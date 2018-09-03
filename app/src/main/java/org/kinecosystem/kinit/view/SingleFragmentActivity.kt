package org.kinecosystem.kinit.view

import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.R

abstract class SingleFragmentActivity : BaseActivity() {

    protected abstract fun getFragment(): Fragment
    var inForeground: Boolean = false
        private set

    open fun beforeSuper() {

    }

    open fun init() {

    }

    override fun onResume() {
        super.onResume()
        inForeground = true
    }

    override fun onPause() {
        super.onPause()
        inForeground = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuper()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_fragment_layout)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, getFragment()).commitNowAllowingStateLoss()
        init()
    }

    fun replaceFragment(fragment: Fragment, withSlideAnimation: Boolean = false, forwardAnimation: Boolean = true) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (withSlideAnimation) {
            if (forwardAnimation) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out)
            }
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment, "TAG").commitNowAllowingStateLoss()
    }
}