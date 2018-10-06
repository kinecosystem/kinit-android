package org.kinecosystem.kinit.viewmodel

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.daggerTestCore.TestCoreComponent
import org.kinecosystem.kinit.daggerTestCore.TestCoreComponentProvider
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.viewmodel.restore.RestoreState
import org.kinecosystem.kinit.viewmodel.restore.RestoreWalletViewModel
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject


@RunWith(MockitoJUnitRunner::class)
class RestoreViewModelTest {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var walletService: Wallet


    private lateinit var restoreWalletViewModel: RestoreWalletViewModel
    private lateinit var coreComponent: TestCoreComponent

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        coreComponent = TestCoreComponentProvider.instance.coreComponent
        KinitApplication.coreComponent = coreComponent
        coreComponent.inject(this)

        `when`(userRepository.restoreHints).thenReturn(listOf("1", "2"))
    }

    @Test
    fun test_init() {
        restoreWalletViewModel = RestoreWalletViewModel()
        assertThat(restoreWalletViewModel.getState()).isEqualTo(RestoreState.Welcomeback)
        assertThat(restoreWalletViewModel.answers).asList().containsExactly("", "")
    }
}