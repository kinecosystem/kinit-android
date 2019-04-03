package org.kinecosystem.kinit.dagger

import dagger.Component
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.firebase.KinInstanceIdService
import org.kinecosystem.kinit.firebase.KinMessagingService
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.TasksRepo
import org.kinecosystem.kinit.util.SupportUtil
import org.kinecosystem.kinit.view.BottomTabNavigation
import org.kinecosystem.kinit.view.MainActivity
import org.kinecosystem.kinit.view.SplashActivity
import org.kinecosystem.kinit.view.TabsAdapter
import org.kinecosystem.kinit.view.adapter.CategoryListAdapter
import org.kinecosystem.kinit.view.adapter.CouponsListAdapter
import org.kinecosystem.kinit.view.adapter.TransactionsListAdapter
import org.kinecosystem.kinit.view.backup.BackupQRCodeFragment
import org.kinecosystem.kinit.view.backup.BackupQuestionAnswerFragment
import org.kinecosystem.kinit.view.backup.BackupSummaryFragment
import org.kinecosystem.kinit.view.spend.EcoAppsInfoActivity
import org.kinecosystem.kinit.view.bootWallet.BootWalletActivity
import org.kinecosystem.kinit.view.bootWallet.BootWalletErrorFragment
import org.kinecosystem.kinit.view.bootWallet.BootWalletFragment
import org.kinecosystem.kinit.view.bootWallet.BootCompleteFragment
import org.kinecosystem.kinit.view.customView.QuizAnswerView
import org.kinecosystem.kinit.view.customView.SecurityQuestionAnswerView
import org.kinecosystem.kinit.view.customView.TransactionLayoutView
import org.kinecosystem.kinit.view.earn.CategoryTaskActivity
import org.kinecosystem.kinit.view.earn.QuestionnaireActivity
import org.kinecosystem.kinit.view.earn.TaskErrorFragment
import org.kinecosystem.kinit.view.earn.WebTaskTruexFragment
import org.kinecosystem.kinit.view.support.SupportActivity
import org.kinecosystem.kinit.view.phoneVerify.CodeVerificationFragment
import org.kinecosystem.kinit.view.phoneVerify.PhoneSendFragment
import org.kinecosystem.kinit.view.phoneVerify.PhoneVerifyActivity
import org.kinecosystem.kinit.view.restore.*
import org.kinecosystem.kinit.view.spend.PurchaseOfferFragment
import org.kinecosystem.kinit.view.transfer.TransferActivity
import org.kinecosystem.kinit.view.tutorial.TutorialActivity
import org.kinecosystem.kinit.viewmodel.*
import org.kinecosystem.kinit.viewmodel.backup.BackupAlertManager
import org.kinecosystem.kinit.viewmodel.backup.BackupModel
import org.kinecosystem.kinit.viewmodel.balance.BalanceViewModel
import org.kinecosystem.kinit.viewmodel.earn.*
import org.kinecosystem.kinit.viewmodel.info.InfoViewModel
import org.kinecosystem.kinit.viewmodel.walletBoot.RestoreWalletViewModel
import org.kinecosystem.kinit.viewmodel.spend.*
import org.kinecosystem.kinit.viewmodel.walletBoot.CreateWalletViewModel
import org.kinecosystem.kinit.viewmodel.walletBoot.MigrateWalletViewModel
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
    fun inject(app: KinitApplication)
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
    fun inject(bootCompleteFragment: BootCompleteFragment)
    fun inject(bootWalletFragment: BootWalletFragment)
    fun inject(bootWalletErrorFragment: BootWalletErrorFragment)
    fun inject(createWalletViewModel: CreateWalletViewModel)
    fun inject(bootWalletActivity: BootWalletActivity)
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
    fun inject(supportViewModel: SupportViewModel)
    fun inject(supportActivity: SupportActivity)
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
    fun inject(ecoAppsComingSoonViewModel: WebInfoViewModel)
    fun inject(ecoAppsComingSoonActivity: EcoAppsInfoActivity)
    fun inject(transferActivity: TransferActivity)
    fun inject(transferToAppViewModel: TransferToAppViewModel)
    fun inject(transferAmountToAppViewModel: TransferringToAppViewModel)
    fun inject(transferActivityModel: TransferActivityModel)
    fun inject(accountInfoViewModel: AccountInfoViewModel)
    fun inject(migrationWalletActivity: MigrateWalletViewModel)
}