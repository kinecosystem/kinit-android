package org.kinecosystem.kinit.view.restore

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.answer_hints_fragment.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.databinding.AnswerHintsFragmentBinding
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.customView.SecurityQuestionAnswerView
import org.kinecosystem.kinit.viewmodel.walletBoot.RestoreWalletViewModel
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
        KinitApplication.coreComponent.inject(this)
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