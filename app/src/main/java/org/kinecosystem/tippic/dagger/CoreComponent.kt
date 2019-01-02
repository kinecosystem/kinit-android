package org.kinecosystem.tippic.dagger

import dagger.Component
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.firebase.KinInstanceIdService
import org.kinecosystem.tippic.firebase.KinMessagingService
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.repository.TasksRepo
import org.kinecosystem.tippic.util.SupportUtil
import org.kinecosystem.tippic.view.BottomTabNavigation
import org.kinecosystem.tippic.view.MainActivity
import org.kinecosystem.tippic.view.SplashActivity
import org.kinecosystem.tippic.view.TabsAdapter
import org.kinecosystem.tippic.view.adapter.CategoryListAdapter
import org.kinecosystem.tippic.view.adapter.CouponsListAdapter
import org.kinecosystem.tippic.view.adapter.TransactionsListAdapter
import org.kinecosystem.tippic.view.backup.BackupQRCodeFragment
import org.kinecosystem.tippic.view.backup.BackupQuestionAnswerFragment
import org.kinecosystem.tippic.view.backup.BackupSummaryFragment
import org.kinecosystem.tippic.view.comingSoon.EcoAppsComingSoonActivity
import org.kinecosystem.tippic.view.createWallet.CreateWalletActivity
import org.kinecosystem.tippic.view.createWallet.CreateWalletErrorFragment
import org.kinecosystem.tippic.view.createWallet.CreateWalletFragment
import org.kinecosystem.tippic.view.createWallet.OnboardingCompleteFragment
import org.kinecosystem.tippic.view.customView.QuizAnswerView
import org.kinecosystem.tippic.view.customView.SecurityQuestionAnswerView
import org.kinecosystem.tippic.view.customView.TransactionLayoutView
import org.kinecosystem.tippic.view.earn.CategoryTaskActivity
import org.kinecosystem.tippic.view.earn.QuestionnaireActivity
import org.kinecosystem.tippic.view.earn.TaskErrorFragment
import org.kinecosystem.tippic.view.earn.WebTaskTruexFragment
import org.kinecosystem.tippic.view.faq.FAQActivity
import org.kinecosystem.tippic.view.phoneVerify.CodeVerificationFragment
import org.kinecosystem.tippic.view.phoneVerify.PhoneSendFragment
import org.kinecosystem.tippic.view.phoneVerify.PhoneVerifyActivity
import org.kinecosystem.tippic.view.restore.*
import org.kinecosystem.tippic.view.spend.PurchaseOfferFragment
import org.kinecosystem.tippic.view.tutorial.TutorialActivity
import org.kinecosystem.tippic.viewmodel.*
import org.kinecosystem.tippic.viewmodel.backup.BackupAlertManager
import org.kinecosystem.tippic.viewmodel.backup.BackupModel
import org.kinecosystem.tippic.viewmodel.balance.BalanceViewModel
import org.kinecosystem.tippic.viewmodel.earn.*
import org.kinecosystem.tippic.viewmodel.info.InfoViewModel
import org.kinecosystem.tippic.viewmodel.restore.RestoreWalletViewModel
import org.kinecosystem.tippic.viewmodel.spend.*
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(ContextModule::class), (UserRepositoryModule::class), (OffersRepositoryModule::class), (EcoAppsRepositoryModule::class), (AnalyticsModule::class), (SchedulerModule::class), (NotificationModule::class), (DataStoreModule::class), (ServicesModule::class)])
interface CoreComponent {

    fun inject(balanceViewModel: BalanceViewModel)
    fun inject(spendViewModel: OffersViewModel)
    fun inject(tabsAdapter: TabsAdapter)
    fun inject(transactionsListAdapter: TransactionsListAdapter)
    fun inject(couponsListAdapter: CouponsListAdapter)
    fun inject(purchaseOfferViewModel: PurchaseOfferViewModel)
    fun inject(splashViewModel: SplashViewModel)
    fun inject(questionnaireViewModel: QuestionnaireViewModel)
    fun inject(questionnaireCompleteViewModel: QuestionnaireCompleteViewModel)
    fun inject(taskRewardViewModel: TaskRewardViewModel)
    fun inject(questionViewModel: QuestionViewModel)
    fun inject(peer2PeerViewModel: Peer2PeerViewModel)
    fun inject(phoneVerificationViewModel: PhoneVerificationViewModel)
    fun inject(kinMessagingService: KinMessagingService)
    fun inject(infoViewModel: InfoViewModel)
    fun inject(kinInstanceIdService: KinInstanceIdService)
    fun inject(tutorialActivity: TutorialActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(phoneVerifyActivity: PhoneVerifyActivity)
    fun inject(phoneSendFragment: PhoneSendFragment)
    fun inject(taskErrorFragment: TaskErrorFragment)
    fun inject(codeVerificationFragment: CodeVerificationFragment)
    fun inject(questionnaireActivity: QuestionnaireActivity)
    fun inject(app: TippicApplication)
    fun inject(splashActivity: SplashActivity)
    fun inject(transactionLayoutView: TransactionLayoutView)
    fun inject(bottomTabNavigation: BottomTabNavigation)
    fun inject(navigator: Navigator)
    fun inject(trueXWebFragment: WebTaskTruexFragment)
    fun inject(webModel: WebViewModel)
    fun inject(trueXModel: WebTaskTruexViewModel)
    fun inject(questionDualFragmentViewModel: QuestionDualImageViewModel)
    fun inject(purchaseOfferFragment: PurchaseOfferFragment)
    fun inject(quizViewModel: QuizQuestionViewModel)
    fun inject(answerViewModel: AnswerViewModel)
    fun inject(quizAnswerView: QuizAnswerView)
    fun inject(onboardingCompleteFragment: OnboardingCompleteFragment)
    fun inject(createWalletFragment: CreateWalletFragment)
    fun inject(createWalletErrorFragment: CreateWalletErrorFragment)
    fun inject(createWalletViewModel: CreateWalletViewModel)
    fun inject(createWalletActivity: CreateWalletActivity)
    fun inject(backupModel: BackupModel)
    fun inject(backupAlertManager: BackupAlertManager)
    fun inject(securityQuestionAnswerView: SecurityQuestionAnswerView)
    fun inject(restoreWalletBarcodeScannerFragment: RestoreWalletBarcodeScannerFragment)
    fun inject(restoreWalletViewModel: RestoreWalletViewModel)
    fun inject(restoreWalletActivity: RestoreWalletActivity)
    fun inject(restoreWalletIntroFragment: RestoreWalletIntroFragment)
    fun inject(restoreWalletWelcomebackFragment: RestoreWalletWelcomebackFragment)
    fun inject(restoreWalletAnswerHintsFragment: RestoreWalletAnswerHintsFragment)
    fun inject(backupQuestionAnswerFragment: BackupQuestionAnswerFragment)
    fun inject(backupQRCodeFragment: BackupQRCodeFragment)
    fun inject(backupSummaryFragment: BackupSummaryFragment)
    fun inject(faqViewModel: FAQViewModel)
    fun inject(faqActivity: FAQActivity)
    fun inject(supportUtil: SupportUtil)
    fun inject(categoryRepository: CategoriesRepository)
    fun inject(categoriesViewModel: CategoriesViewModel)
    fun inject(categoryListAdapter: CategoryListAdapter)
    fun inject(categoryTaskActivity: CategoryTaskActivity)
    fun inject(categoryTaskViewModel: CategoryTaskViewModel)
    fun inject(tasksRepo: TasksRepo)
    fun inject(spendTabViewModel: SpendTabsViewModel)
    fun inject(transferViewModel: EcoAppsViewModel)
    fun inject(appViewModel: AppViewModel)
    fun inject(ecoAppsViewModel: EcoAppsCategoryViewModel)
    fun inject(ecoAppsComingSoonViewModel: EcoAppsComingSoonViewModel)
    fun inject(ecoAppsComingSoonActivity: EcoAppsComingSoonActivity)

}