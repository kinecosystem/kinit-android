package org.kinecosystem.kinit.viewmodel

import com.google.common.truth.Truth.assertThat
import kin.sdk.KinAccount
import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.daggerCore.TestUtils
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.ERROR_APP_SERVER_FAILED_RESPONSE
import org.kinecosystem.kinit.server.OnboardingService
import org.kinecosystem.kinit.server.OperationCompletionCallback
import org.kinecosystem.kinit.viewmodel.walletBoot.RestoreWalletActivityMessages
import org.kinecosystem.kinit.viewmodel.walletBoot.RestoreWalletViewModel
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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestUtils.setupCoreComponent().inject(this)
        `when`(userRepository.restoreHints).thenReturn(listOf("1", "2"))
        restoreWalletViewModel = RestoreWalletViewModel()
    }

    @Test
    fun nextEnabled_False_When_setAnswer_shortInput() {
        restoreWalletViewModel.setAnswer(0, "")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isFalse()
        restoreWalletViewModel.setAnswer(0, "1234")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isFalse()
        restoreWalletViewModel.setAnswer(0, "")
        restoreWalletViewModel.setAnswer(1, "1234")
        assertThat(restoreWalletViewModel.nextEnabled.get()).isFalse()
    }

    @Test
    fun nextEnabled_True_When_setAnswer_longInput() {
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