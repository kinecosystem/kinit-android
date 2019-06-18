package org.kinecosystem.kinit.view.bootWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import kotlinx.android.synthetic.main.boot_wallet_complete_fragment.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.restore.RestoreWalletActions
import javax.inject.Inject

class BootCompleteFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics
    var listener: AfterAnimationListener? = null

    interface AfterAnimationListener {
        fun onAnimationEnd()
    }

    companion object {
        val TAG = BootCompleteFragment::class.java.simpleName
        const val ANIMATION_DURATION: Long = 5000
        @JvmStatic
        fun newInstance(): BootCompleteFragment {
            return BootCompleteFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.boot_wallet_complete_fragment, container, false)
        view.postDelayed({ listener?.onAnimationEnd() }, ANIMATION_DURATION)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_konfetti.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (view_konfetti != null && view_konfetti.width > 0) {
                    view_konfetti.viewTreeObserver.removeOnGlobalLayoutListener { this.onGlobalLayout() }
                    startKonfettiAnim()
                }
            }
        })
    }

    private fun startKonfettiAnim() {
        view_konfetti.build()
                .addColors(resources.getColor(R.color.konfetti1), resources.getColor(R.color.konfetti2),
                        resources.getColor(R.color.konfetti3), resources.getColor(R.color.konfetti4))
                .setDirection(0.0, 359.0)
                .setSpeed(0f, 8f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(10, 5f))
                .setPosition(-50f, view_konfetti.width + 50f, -50f, -50f)
                .stream(200, ANIMATION_DURATION)
    }

    override fun onResume() {
        super.onResume()
        if (activity is RestoreWalletActions)
            analytics.logEvent(Events.Analytics.ViewWalletRestoredPage())
        else
            analytics.logEvent(Events.Analytics.ViewOnboardingCompletedPage())
    }
}