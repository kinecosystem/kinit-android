package org.kinecosystem.kinit.mock

import android.content.Context
import dagger.CoreComponent
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.network.Wallet
import org.kinecosystem.kinit.network.firebase.KinInstanceIdService
import org.kinecosystem.kinit.network.firebase.KinMessagingService
import org.kinecosystem.kinit.notification.NotificationPublisher
import org.kinecosystem.kinit.repository.*
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.MainActivity
import org.kinecosystem.kinit.view.TabsAdapter
import org.kinecosystem.kinit.view.adapter.CouponsListAdapter
import org.kinecosystem.kinit.view.adapter.TransactionsListAdapter
import org.kinecosystem.kinit.view.earn.QuestionnaireActivity
import org.kinecosystem.kinit.view.earn.TaskErrorFragment
import org.kinecosystem.kinit.view.phoneVerify.CodeVerificationFragment
import org.kinecosystem.kinit.view.phoneVerify.PhoneAuthCompleteFragment
import org.kinecosystem.kinit.view.phoneVerify.PhoneSendFragment
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity
import org.kinecosystem.kinit.view.spend.PurchaseOfferFragment
import org.kinecosystem.kinit.view.tutorial.TutorialActivity
import org.kinecosystem.kinit.viewmodel.PhoneVerificationViewModel
import org.kinecosystem.kinit.viewmodel.SplashViewModel
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel
import org.kinecosystem.kinit.viewmodel.earn.QuestionViewModel
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireCompleteViewModel
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireRewardViewModel
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireViewModel
import org.kinecosystem.kinit.viewmodel.info.InfoViewModel
import org.kinecosystem.kinit.viewmodel.spend.Peer2PeerViewModel
import org.kinecosystem.kinit.viewmodel.spend.PurchaseOfferViewModel
import org.kinecosystem.kinit.viewmodel.spend.SpendViewModel
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class MockComponentsProvider : CoreComponent, DataStoreProvider {

    var userRepository: UserRepository = Mockito.mock(UserRepository::class.java)
    var questionnaireRepository = Mockito.mock(QuestionnaireRepository::class.java)
    var offersRepository = Mockito.mock(OffersRepository::class.java)
    var servicesProvider: ServicesProvider = Mockito.mock(ServicesProvider::class.java)
    var analytics: org.kinecosystem.kinit.analytics.Analytics = Mockito.mock(Analytics::class.java)
    var scheduler = MockScheduler()
    var wallet: Wallet = Mockito.mock(Wallet::class.java)
    var notificationPublisher: NotificationPublisher = Mockito.mock(NotificationPublisher::class.java)

    init {
        `when`(servicesProvider.walletService).thenReturn(wallet)
    }

    override fun analytics(): Analytics {
        return analytics
    }

    override fun notificationPublisher(): NotificationPublisher {
        return notificationPublisher
    }

    override fun context(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun userRepository(): UserRepository {
        return userRepository
    }

    override fun questionnaireRepository(): QuestionnaireRepository {
        return questionnaireRepository
    }

    override fun offersRepository(): OffersRepository {
        return offersRepository
    }

    override fun scheduler(): Scheduler {
        return scheduler
    }

    override fun dataStoreProvider(): DataStoreProvider {
        return this
    }


    override fun servicesProvider(): ServicesProvider {
        return servicesProvider
    }

    override fun dataStore(storage: String): DataStore {
        return MockDataStore()
    }


    override fun inject(balanceViewModel: BalanceViewModel) {

    }

    override fun inject(spendViewModel: SpendViewModel) {

    }

    override fun inject(tabsAdapter: TabsAdapter) {

    }

    override fun inject(transactionsListAdapter: TransactionsListAdapter) {

    }

    override fun inject(couponsListAdapter: CouponsListAdapter) {
    }

    override fun inject(purchaseOfferViewModel: PurchaseOfferViewModel) {

    }

    override fun inject(splashViewModel: SplashViewModel) {

    }

    override fun inject(questionnaireViewModel: QuestionnaireViewModel) {

    }

    override fun inject(questionnaireCompleteViewModel: QuestionnaireCompleteViewModel) {

    }

    override fun inject(questionnaireRewardViewModel: QuestionnaireRewardViewModel) {

    }

    override fun inject(questionViewModel: QuestionViewModel) {

    }

    override fun inject(peer2PeerViewModel: Peer2PeerViewModel) {

    }

    override fun inject(phoneVerificationViewModel: PhoneVerificationViewModel) {

    }

    override fun inject(kinMessagingService: KinMessagingService) {

    }

    override fun inject(infoViewModel: InfoViewModel) {

    }

    override fun inject(kinInstanceIdService: KinInstanceIdService) {

    }

    override fun inject(tutorialActivity: TutorialActivity) {

    }

    override fun inject(mainActivity: MainActivity) {

    }

    override fun inject(phoneVerifyActivity: PhoneVerifyActivity) {

    }

    override fun inject(phoneSendFragment: PhoneSendFragment) {

    }

    override fun inject(purchaseOfferFragment: PurchaseOfferFragment) {

    }

    override fun inject(taskErrorFragment: TaskErrorFragment) {

    }

    override fun inject(codeVerificationFragment: CodeVerificationFragment) {

    }

    override fun inject(phoneAuthCompleteFragment: PhoneAuthCompleteFragment) {

    }

    override fun inject(questionnaireActivity: QuestionnaireActivity) {

    }
}
