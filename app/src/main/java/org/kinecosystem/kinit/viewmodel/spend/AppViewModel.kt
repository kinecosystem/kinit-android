package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableInt
import android.support.v4.view.ViewPager
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.model.spend.getHeaderImageCount
import org.kinecosystem.kinit.model.spend.getHeaderImageUrl
import org.kinecosystem.kinit.model.spend.isKinTransferSupported
import org.kinecosystem.kinit.navigation.Navigator
import javax.inject.Inject

class AppViewModel(private val navigator: Navigator, private val app: EcoApplication) : ViewPager.OnPageChangeListener {

    @Inject
    lateinit var analytics: Analytics


    val appIconUrl: String = app.data.iconUrl
    val appName: String = app.name
    val shortDescription = app.data.descriptionShort
    val categoryTitle = app.data.categoryTitle
    val aboutApp = app.data.description
    val kinUsage = app.data.kinUsage
    val canTransferKin = app.isKinTransferSupported()

    val headerImagesCount = app.getHeaderImageCount()
    val showPagesIndicator = app.getHeaderImageCount() >= 2
    val pageSelected = ObservableInt(0)


    init {
        KinitApplication.coreComponent.inject(this)
    }

    fun getHeaderImageUrl(position: Int) = app.getHeaderImageUrl(position)

    fun onCloseButtonClicked(view: View?) {
        navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
    }

    fun onActionBtnClicked(view: View?) {
        if (canTransferKin) {
            analytics.logEvent(Events.Analytics.ClickSendButtonOnAppPage(app.data.categoryTitle, app.identifier, app.name, app.isKinTransferSupported()))
        } else {
            navigator.navigateToUrl(app.data.appUrl)
            analytics.logEvent(Events.Analytics.ClickGetButtonOnAppPage(app.data.categoryTitle, app.identifier, app.name, app.isKinTransferSupported()))
        }
    }

    fun onResume() {
        analytics.logEvent(Events.Analytics.ViewAppPage(app.data.categoryTitle, app.identifier, app.name, app.isKinTransferSupported()))
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(page: Int) {
        pageSelected.set(page)
    }

    override fun onPageScrollStateChanged(position: Int) {
    }


}