package org.kinecosystem.kinit.view.earn

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.doOnPreDraw
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuestionnaireRewardFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.QuestionnaireRewardViewModel

interface TransactionTimeout {
    fun onTransactionTimeout()
}

class QuestionnaireRewardFragment : BaseFragment(), TransactionTimeout {
    var model: QuestionnaireRewardViewModel? = null
    lateinit var binding: QuestionnaireRewardFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.questionnaire_reward_fragment, container,
            false)
        model = QuestionnaireRewardViewModel(coreComponents, this)
        binding.model = model
        binding.transactionImage.doOnPreDraw {
            animateIn()
        }

        return binding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        model?.attach(this)
    }

    override fun onDetach() {
        super.onDetach()
        model?.detach()
    }

    private fun animateIn() {
        val view = binding.transactionImage
        view.y = view.y + view.height
        view.animate().translationYBy((-view.height).toFloat())
            .setDuration(500L)
            .setInterpolator(AccelerateDecelerateInterpolator())
    }

    override fun onResume() {
        super.onResume()
        model?.onResume()
    }

    companion object {
        fun newInstance(): QuestionnaireRewardFragment {
            return QuestionnaireRewardFragment()
        }
    }

    override fun onTransactionTimeout() {
        if (activity != null && activity is QuestionnaireActions) {
            (activity as QuestionnaireActions).transactionError()
        }
    }

}
