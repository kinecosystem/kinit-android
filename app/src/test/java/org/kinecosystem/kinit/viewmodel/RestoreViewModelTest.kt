package org.kinecosystem.kinit.viewmodel

import com.google.common.truth.Truth.assertThat
import kin.core.KinAccount
import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.daggerCore.TestCoreComponentProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.ERROR_APP_SERVER_FAILED_RESPONSE
import org.kinecosystem.kinit.server.OnboardingService
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.viewmodel.restore.RestoreWalletActivityMessages
import org.kinecosystem.kinit.viewmodel.restore.RestoreWalletViewModel
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import javax.inject.Inject


private const val QR_SAMPLE_CODE = "abcdefghijklmnopqrstuvwxyzzyxwvutsrqponmlkjihgfedcba"
private const val QR_SAMPLE_CODE_ANSWER_ONE = "1234"
private const val QR_SAMPLE_CODE_ANSWER_TWO = "1234"

class RestoreViewModelTest {
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var onboardingService: OnboardingService
    @Inject
    lateinit var walletService: Wallet

    private lateinit var restoreWalletViewModel: RestoreWalletViewModel
    private lateinit var coreComponentProvider: TestCoreComponentProvider

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        coreComponentProvider = TestCoreComponentProvider()
        KinitApplication.coreComponent = coreComponentProvider.coreComponent
        coreComponentProvider.coreComponent.inject(this)

        `when`(userRepository.restoreHints).thenReturn(listOf("1", "2"))
        restoreWalletViewModel = RestoreWalletViewModel()
    }

    @Test
    fun nextEnabledFalse_When_setAnswer_shortInput() {
        restoreWalletViewModel.setAnswer(0, "")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isFalse()
        restoreWalletViewModel.setAnswer(0, "1234")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isFalse()
        restoreWalletViewModel.setAnswer(0, "")
        restoreWalletViewModel.setAnswer(1, "1234")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isFalse()
    }

    @Test
    fun nextEnabledTrue_When_setAnswer_longInput() {
        restoreWalletViewModel.setAnswer(0, "1234")
        restoreWalletViewModel.setAnswer(1, "1234")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isTrue()
    }

    @Test
    fun onError_When_onAnswersSubmit_BadPassphrase() {
        restoreWalletViewModel.setAnswer(0, "")
        restoreWalletViewModel.setAnswer(1, "")
        restoreWalletViewModel.qrCode = QR_SAMPLE_CODE
        restoreWalletViewModel.listener = mock(RestoreWalletViewModel.RestoreWalletViewModelListener::class.java)
        `when`(walletService.importBackedUpAccount(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null)

        restoreWalletViewModel.onAnswersSubmit()

        assertThat(restoreWalletViewModel.answersSubmitted.get()).isFalse()
        verify(restoreWalletViewModel.listener, times(1))?.onError(RestoreWalletActivityMessages.RESTORE_ERROR)
    }

    @Test
    fun onError_When_onAnswersSubmit_restoreAccount_onError() {
        restoreWalletViewModel.setAnswer(0, QR_SAMPLE_CODE_ANSWER_ONE)
        restoreWalletViewModel.setAnswer(1, QR_SAMPLE_CODE_ANSWER_TWO)
        restoreWalletViewModel.qrCode = QR_SAMPLE_CODE
        restoreWalletViewModel.listener = mock(RestoreWalletViewModel.RestoreWalletViewModelListener::class.java)
        val mockKinAccount = mock(KinAccount::class.java)

        `when`(walletService.importBackedUpAccount(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(mockKinAccount)
        `when`(mockKinAccount.publicAddress).thenReturn("")

        doAnswer { invocation ->
            val callback = invocation.getArgument<OperationCompletionCallback>(1)
            callback.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
        }.`when`(onboardingService).restoreAccount(ArgumentMatchers.anyString(), any(OperationCompletionCallback::class.java))

        restoreWalletViewModel.onAnswersSubmit()

        verify(restoreWalletViewModel.listener, times(1))?.onError(RestoreWalletActivityMessages.RESTORE_SERVER_ERROR)
        assertThat(restoreWalletViewModel.answersSubmitted.get()).isFalse()
    }

    @Test
    fun onSuccess_When_onAnswersSubmit_restoreAccount_onSuccess() {
        restoreWalletViewModel.setAnswer(0, QR_SAMPLE_CODE_ANSWER_ONE)
        restoreWalletViewModel.setAnswer(1, QR_SAMPLE_CODE_ANSWER_TWO)
        restoreWalletViewModel.qrCode = QR_SAMPLE_CODE
        restoreWalletViewModel.listener = mock(RestoreWalletViewModel.RestoreWalletViewModelListener::class.java)
        val mockKinAccount = mock(KinAccount::class.java)

        `when`(walletService.importBackedUpAccount(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(mockKinAccount)
        `when`(mockKinAccount.publicAddress).thenReturn("")

        doAnswer { invocation ->
            val callback = invocation.getArgument<OperationCompletionCallback>(1)
            callback.onSuccess()
        }.`when`(onboardingService).restoreAccount(ArgumentMatchers.anyString(), any(OperationCompletionCallback::class.java))

        restoreWalletViewModel.onAnswersSubmit()

        verify(restoreWalletViewModel.listener, times(1))?.onSuccess()
        assertThat(restoreWalletViewModel.answersSubmitted.get()).isTrue()
    }
}