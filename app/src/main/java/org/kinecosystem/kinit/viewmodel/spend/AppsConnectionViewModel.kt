package org.kinecosystem.kinit.viewmodel.spend

import org.kinecosystem.kinit.model.spend.EcosystemApp

class AppsConnectionViewModel(app: EcosystemApp) {
    val appIconUrl: String = app.data.iconUrl
}