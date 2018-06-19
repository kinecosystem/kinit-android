package org.kinecosystem.kinit.view.tutorial

import android.os.Bundle
import android.support.v4.app.Fragment
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.BaseActivity

abstract class BaseSingleFragmentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_fragment_layout)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, getFragment()).commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment).commit()
    }

    abstract fun getFragment(): Fragment
    abstract fun inject()
}