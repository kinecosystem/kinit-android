package org.kinecosystem.kinit.view

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment


class RegisterErrorActivity  : SingleFragmentActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, RegisterErrorActivity::class.java)
    }

    override fun getFragment(): Fragment {
        return RegisterErrorFragment.newInstance()
    }
}