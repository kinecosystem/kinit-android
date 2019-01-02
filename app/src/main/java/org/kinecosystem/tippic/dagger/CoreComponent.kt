package org.kinecosystem.tippic.dagger

import dagger.Component
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.firebase.KinInstanceIdService
import org.kinecosystem.tippic.firebase.KinMessagingService
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.util.SupportUtil
import org.kinecosystem.tippic.view.MainActivity
import org.kinecosystem.tippic.view.SplashActivity
import org.kinecosystem.tippic.view.createWallet.CreateWalletActivity
import org.kinecosystem.tippic.view.createWallet.CreateWalletErrorFragment
import org.kinecosystem.tippic.view.createWallet.CreateWalletFragment
import org.kinecosystem.tippic.view.createWallet.OnboardingCompleteFragment
import org.kinecosystem.tippic.view.phoneVerify.CodeVerificationFragment
import org.kinecosystem.tippic.view.phoneVerify.PhoneSendFragment
import org.kinecosystem.tippic.view.phoneVerify.PhoneVerifyActivity
import org.kinecosystem.tippic.viewmodel.CreateWalletViewModel
import org.kinecosystem.tippic.viewmodel.PhoneVerificationViewModel
import org.kinecosystem.tippic.viewmodel.SplashViewModel
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(ContextModule::class), (UserRepositoryModule::class), (AnalyticsModule::class), (SchedulerModule::class), (NotificationModule::class), (DataStoreModule::class), (ServicesModule::class)])
interface CoreComponent {

    fun inject(splashViewModel: SplashViewModel)
    fun inject(phoneVerificationViewModel: PhoneVerificationViewModel)
    fun inject(kinMessagingService: KinMessagingService)
    fun inject(kinInstanceIdService: KinInstanceIdService)
    fun inject(mainActivity: MainActivity)
    fun inject(phoneVerifyActivity: PhoneVerifyActivity)
    fun inject(phoneSendFragment: PhoneSendFragment)
    fun inject(codeVerificationFragment: CodeVerificationFragment)
    fun inject(app: TippicApplication)
    fun inject(splashActivity: SplashActivity)
    fun inject(navigator: Navigator)
    fun inject(onboardingCompleteFragment: OnboardingCompleteFragment)
    fun inject(createWalletFragment: CreateWalletFragment)
    fun inject(createWalletErrorFragment: CreateWalletErrorFragment)
    fun inject(createWalletViewModel: CreateWalletViewModel)
    fun inject(createWalletActivity: CreateWalletActivity)
    fun inject(supportUtil: SupportUtil)

}