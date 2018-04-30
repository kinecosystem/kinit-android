package org.kinecosystem.kinit.view

import android.support.v7.app.AppCompatActivity
import org.kinecosystem.kinit.CoreComponentsProvider

open class BaseActivity : AppCompatActivity() {

    val coreComponents: CoreComponentsProvider
        get() = applicationContext as CoreComponentsProvider
}
