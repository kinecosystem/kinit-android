package org.kinecosystem.kinit.view.earn

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.view.doOnPreDraw
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuestionnaireCompleteFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireCompleteViewModel

class QuestionnaireCompleteFragment : BaseFragment() {

    private lateinit var model: QuestionnaireCompleteViewModel
    private lateinit var binding: QuestionnaireCompleteFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.questionnaire_complete_fragment,
                container, false)
        model = QuestionnaireCompleteViewModel()
        binding.model = model
        binding.hands.doOnPreDraw {
            animateIn()
        }

        return binding.root
    }

    private fun animateIn() {
        val fireworks = binding.fireworks
        val hands = binding.hands
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
        binding.subtitle.animate().alpha(0f).setStartDelay(1500).setDuration(250L)
        binding.hands.animate().translationYBy(binding.hands.height.toFloat()).alpha(0f).setStartDelay(1500)
            .setDuration(250L).withEndAction {
                binding.hands.clearAnimation()
                if (activity != null && activity is QuestionnaireActions) {
                    if (model.submitComplete) {
                        (activity as QuestionnaireActions).submissionComplete()
                    } else {
                        (activity as QuestionnaireActions).submissionError()
                    }

                }
            }

        binding.title.animate().alpha(0f).setStartDelay(1500).setDuration(250L)
        binding.fireworks.animate().alpha(0f).setDuration(1250L)


    }

    override fun onResume() {
        super.onResume()
        model.onResume()
        if (activity != null && activity is QuestionnaireActivity) {

            (activity as QuestionnaireActivity).updateNoToolBar()
        }
    }

    companion object {

        fun newInstance(): QuestionnaireCompleteFragment {
            return QuestionnaireCompleteFragment()
        }
    }
}
