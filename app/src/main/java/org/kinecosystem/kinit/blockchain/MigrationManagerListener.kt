package org.kinecosystem.kinit.blockchain

import kin.sdk.migration.bi.IMigrationEventsListener
import kin.sdk.migration.common.KinSdkVersion
import java.lang.Exception

class MigrationManagerListener : IMigrationEventsListener {
    override fun onCheckBurnSucceeded(publicAddress: String?, reason: IMigrationEventsListener.CheckBurnReason?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestAccountMigrationSucceeded(publicAddress: String?, reason: IMigrationEventsListener.RequestAccountMigrationSuccessReason?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVersionCheckFailed(exception: Exception?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallbackReady(sdkVersion: KinSdkVersion?, selectedSdkReason: IMigrationEventsListener.SelectedSdkReason?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBurnSucceeded(publicAddress: String?, reason: IMigrationEventsListener.BurnReason?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBurnFailed(publicAddress: String?, exception: Exception?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallbackStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBurnStarted(publicAddress: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCheckBurnStarted(publicAddress: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVersionCheckStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallbackFailed(exception: Exception?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestAccountMigrationStarted(publicAddress: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVersionCheckSucceeded(sdkVersion: KinSdkVersion?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCheckBurnFailed(publicAddress: String?, exception: Exception?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestAccountMigrationFailed(publicAddress: String?, exception: Exception?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMethodStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}