package org.kinecosystem.kinit.kindiscover

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.transfer.receiver.view.AccountInfoActivityBase
import javax.inject.Inject


class AccountInfoActivity : AccountInfoActivityBase() {

    @Inject
    lateinit var networkServices: NetworkServices
    val address: String

    init {
        KinitApplication.coreComponent.inject(this)
        address = networkServices.walletService.userRepo.userInfo.publicAddress
    }
    override fun getData(): String {
        return address
    }

}