package org.kinecosystem.kinit.view.earn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.view.doOnPreDraw
import kotlinx.android.synthetic.main.questionnaire_complete_fragment.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.BaseFragment

class WebTaskCompleteFragment : BaseFragment() {

    interface TaskCompleteListener {
        fun onAnimationComplete()
    }

    lateinit var listener: TaskCompleteListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.questionnaire_complete_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hands.doOnPreDraw {
            animateIn()
        }
    }

    private fun animateIn() {
        fireworks.alpha = 0f
        fireworks.scaleX = 0.5f
        fireworks.scaleY = 0.5f
        fireworks.animate().alpha(1f).setStartDelay(450L).scaleX(1.3f).scaleY(1.3f).setDuration(650)
                .setInterpolator(OvershootInterpolator(1.5f)).withEndAction {
                    animateOut()
                }
        val gap = hands.height.toFloat()
        hands.y = hands.y + gap
        hands.animate().translationYBy(-gap).setStartDelay(100L).duration = 500
    }

    private fun animateOut() {
        subtitle.animate().alpha(0f).setStartDelay(1500).setDuration(250L)
        hands.animate().translationYBy(hands.height.toFloat()).alpha(0f).setStartDelay(1500)
                .setDuration(250L).withEndAction {
                    hands.clearAnimation()
                    if (listener != null) {
                        listener.onAnimationComplete()
                    }
                }

        title.animate().alpha(0f).setStartDelay(1500).setDuration(250L)
        fireworks.animate().alpha(0f).setDuration(1250L)
    }

    companion object {

        fun newInstance(): WebTaskCompleteFragment {
            return WebTaskCompleteFragment()
        }
    }
}
