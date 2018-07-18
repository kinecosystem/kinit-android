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
import org.kinecosystem.kinit.databinding.TaskRewardFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.TaskRewardViewModel

interface TransactionTimeout {
    fun onTransactionTimeout()
    fun onSubmitError()
}

class TaskRewardFragment : BaseFragment(), TransactionTimeout {

    var model: TaskRewardViewModel? = null
    lateinit var binding: TaskRewardFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.task_reward_fragment, container,
            false)
        model = TaskRewardViewModel(this)
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
        fun newInstance(): TaskRewardFragment {
            return TaskRewardFragment()
        }
    }

    override fun onTransactionTimeout() {
        if (activity != null && activity is TransactionActions) {
            (activity as TransactionActions).transactionError()
        }
    }

    override fun onSubmitError() {
        if (activity != null && activity is QuestionnaireActions) {
            (activity as QuestionnaireActions).submissionError()
        }
    }

}
