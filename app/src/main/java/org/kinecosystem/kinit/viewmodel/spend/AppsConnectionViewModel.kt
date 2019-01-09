package org.kinecosystem.kinit.viewmodel.spend

import org.kinecosystem.kinit.model.spend.EcoApplication

class AppsConnectionViewModel(app: EcoApplication) {
    val appIconUrl: String = app.data.iconUrl
}