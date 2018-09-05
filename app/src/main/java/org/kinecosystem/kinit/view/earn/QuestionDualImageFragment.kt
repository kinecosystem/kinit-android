package org.kinecosystem.kinit.view.earn

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuestionDualImageFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.QuestionDualImageViewModel


class QuestionDualImageFragment : BaseFragment() {

    lateinit var binding: QuestionDualImageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (activity !is QuestionnaireActions) {
            onInvalidData()
        }

        val questionIndex = arguments?.getInt(ARG_QUESTION_INDEX, INVALID_QUESTION_INDEX)
                ?: INVALID_QUESTION_INDEX
        if (questionIndex == INVALID_QUESTION_INDEX) {
            onInvalidData()
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.question_dual_image_fragment, container, false)
        binding.model = QuestionDualImageViewModel(questionIndex, activity as QuestionnaireActions?)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.model?.onResume()
    }

    private fun onInvalidData() {
        Log.e(TAG, "Invalid data cant start ${QuestionDualImageFragment::class.simpleName}")
        activity?.finish()
    }

    companion object {

        val ARG_QUESTION_INDEX = "QuestionnaireFragment_ARG_QUESTION_INDEX"
        val INVALID_QUESTION_INDEX = -1
        val TAG = QuestionDualImageFragment::class.java.simpleName

        fun newInstance(questionIndex: Int): QuestionDualImageFragment {
            val fragment = QuestionDualImageFragment()
            val args = Bundle()
            args.putInt(ARG_QUESTION_INDEX, questionIndex)
            fragment.arguments = args
            return fragment
        }
    }
}
