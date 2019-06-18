package org.kinecosystem.kinit.blockchain

import kin.sdk.migration.bi.IMigrationEventsListener
import kin.sdk.migration.common.KinSdkVersion
import java.lang.Exception

class MigrationManagerListener : IMigrationEventsListener {
    override fun onCheckBurnSucceeded(publicAddress: String?, reason: IMigrationEventsListener.CheckBurnReason?) {}

    override fun onRequestAccountMigrationSucceeded(publicAddress: String?, reason: IMigrationEventsListener.RequestAccountMigrationSuccessReason?) {}

    override fun onVersionCheckFailed(exception: Exception?) {}

    override fun onCallbackReady(sdkVersion: KinSdkVersion?, selectedSdkReason: IMigrationEventsListener.SelectedSdkReason?) {}

    override fun onBurnSucceeded(publicAddress: String?, reason: IMigrationEventsListener.BurnReason?) {}

    override fun onBurnFailed(publicAddress: String?, exception: Exception?) {}

    override fun onCallbackStart() {}

    override fun onBurnStarted(publicAddress: String?) {}

    override fun onCheckBurnStarted(publicAddress: String?) {}

    override fun onVersionCheckStarted() {}

    override fun onCallbackFailed(exception: Exception?) {}

    override fun onRequestAccountMigrationStarted(publicAddress: String?) {}

    override fun onVersionCheckSucceeded(sdkVersion: KinSdkVersion?) {}

    override fun onCheckBurnFailed(publicAddress: String?, exception: Exception?) {}

    override fun onRequestAccountMigrationFailed(publicAddress: String?, exception: Exception?) {}

    override fun onMethodStarted() {}

}