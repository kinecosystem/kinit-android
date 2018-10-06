package org.kinecosystem.kinit.daggerTestCore

import android.content.Context
import org.kinecosystem.kinit.dagger.NavigatorModule
import org.kinecosystem.kinit.navigation.Navigator
import org.mockito.Mockito.mock

class TestNavigatorModule : NavigatorModule() {
    override fun navigator(context: Context): Navigator {
        return mock(Navigator::class.java)
    }
}
