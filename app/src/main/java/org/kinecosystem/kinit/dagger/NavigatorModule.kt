package org.kinecosystem.kinit.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.kinecosystem.kinit.annotations.DebugOpenClass
import org.kinecosystem.kinit.navigation.Navigator
import javax.inject.Singleton


@DebugOpenClass
@Module(includes = [ContextModule::class])
class NavigatorModule{

    @Provides
    @Singleton
    fun navigator(context: Context) : Navigator{
        return Navigator(context)
    }
}
