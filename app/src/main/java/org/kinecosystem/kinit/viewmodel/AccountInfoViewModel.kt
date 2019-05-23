package org.kinecosystem.kinit.viewmodel

import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.KinitApplication
import javax.inject.Inject

class AccountInfoViewModel {

    @Inject
    lateinit var userRepo: UserRepository


    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun getPublicAddress(): String? {
        return userRepo.userInfo.publicAddress
    }

}