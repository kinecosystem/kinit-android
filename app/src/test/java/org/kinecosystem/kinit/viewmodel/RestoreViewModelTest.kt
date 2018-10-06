package org.kinecosystem.kinit.viewmodel

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.daggerCore.TestCoreComponentProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.viewmodel.restore.RestoreState
import org.kinecosystem.kinit.viewmodel.restore.RestoreWalletViewModel
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import javax.inject.Inject


class RestoreViewModelTest {
    @Inject
    lateinit var userRepository: UserRepository
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
    }

    @Test
    fun test_init() {
        restoreWalletViewModel = RestoreWalletViewModel()
        assertThat(restoreWalletViewModel.getState()).isEqualTo(RestoreState.Welcomeback)
        assertThat(restoreWalletViewModel.answers).asList().containsExactly("", "")
    }
}