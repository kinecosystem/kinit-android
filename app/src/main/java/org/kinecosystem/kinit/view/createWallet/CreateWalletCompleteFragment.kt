package org.kinecosystem.kinit.view.createWallet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import kotlinx.android.synthetic.main.create_wallet_complete_fragment.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.view.BaseFragment
import javax.inject.Inject

class CreateWalletCompleteFragment : BaseFragment() {
    @Inject
    lateinit var analytics: Analytics
    private var actions: CreateWalletActions? = null

    companion object {
        val TAG = CreateWalletCompleteFragment::class.java.simpleName
        const val TIMEOUT: Long = 5000
        @JvmStatic
        fun newInstance(): CreateWalletCompleteFragment {
            return CreateWalletCompleteFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.create_wallet_complete_fragment, container, false)
        if (activity is CreateWalletActions?) {
            actions = activity as CreateWalletActions?
        } else {
            Log.e(TAG, "activity must implements CreateWalletActions")
            activity?.finish()
        }
        view.postDelayed({ actions?.moveToMainScreen() }, TIMEOUT)
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
                .stream(200, 5000L)
    }

    override fun onResume() {
        super.onResume()
        analytics.logEvent(Events.Analytics.ViewOnboardingCompletedPage())
    }
}