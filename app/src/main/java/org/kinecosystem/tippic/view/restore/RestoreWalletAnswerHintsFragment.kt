package org.kinecosystem.tippic.view.restore

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.answer_hints_fragment.*
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.databinding.AnswerHintsFragmentBinding
import org.kinecosystem.tippic.util.Scheduler
import org.kinecosystem.tippic.view.BaseFragment
import org.kinecosystem.tippic.view.customView.SecurityQuestionAnswerView
import org.kinecosystem.tippic.viewmodel.restore.RestoreWalletViewModel
import javax.inject.Inject


class RestoreWalletAnswerHintsFragment : BaseFragment() {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var scheduler: Scheduler

    private lateinit var actions: RestoreWalletActions
    private lateinit var model: RestoreWalletViewModel

    companion object {
        val TAG = RestoreWalletAnswerHintsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): RestoreWalletAnswerHintsFragment {
            return RestoreWalletAnswerHintsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        actions.getModel().onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            actions = activity as RestoreWalletActions
            model = (activity as RestoreWalletActivity).getModel()
        } catch (e: Exception) {
            Log.e(TAG, "activity must be BarcodeScannerFragment and implement WalletCreationActions")
            activity?.finish()
        }
        val binding = DataBindingUtil.inflate<AnswerHintsFragmentBinding>(LayoutInflater.from(context),
                R.layout.answer_hints_fragment, container, false)
        binding.model = model
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        TippicApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        analytics.protectView(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity !is RestoreWalletActions) {
            Log.e(TAG, "Activity must implement RestoreActions")
            activity?.finish()
        }
        activity?.let {
            val hints = model.userRepository.restoreHints
            for (index in hints.indices) {
                val singleHintView = SecurityQuestionAnswerView(it, model, index, hints[index].toInt())
                answers_wrapper?.addView(singleHintView, index)
            }
        }
        backBtn.setOnClickListener {
            actions.moveBack()
            model.answersSubmitted.set(false)
        }
    }
}