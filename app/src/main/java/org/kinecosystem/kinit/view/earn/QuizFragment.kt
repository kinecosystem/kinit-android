package org.kinecosystem.kinit.view.earn


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.QuizFragmentBinding
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.viewmodel.earn.QuizQuestionViewModel


class QuizFragment : BaseFragment() {
    private var quizModel: QuizQuestionViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil
                .inflate<QuizFragmentBinding>(inflater, R.layout.quiz_fragment, container, false)
        if (activity !is QuestionnaireActions) {
            onInvalidData()
        }
        if (arguments == null || !arguments!!.containsKey(ARG_QUIZ_INDEX)) {
            onInvalidData()
        }
        val questionIndex = arguments!!.getInt(ARG_QUIZ_INDEX, INVALID_QUIZ_INDEX)
        if (questionIndex == INVALID_QUIZ_INDEX) {
            onInvalidData()
        }
        val questionnaireActions = activity as QuestionnaireActions?
        quizModel = QuizQuestionViewModel(questionIndex, questionnaireActions!!)
        binding.model = quizModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        quizModel?.onResume()
    }

    private fun onInvalidData() {
        Log.e(TAG, "Invalid data cant start QuestionFragment")
        activity?.finish()
    }

    companion object {

        const val ARG_QUIZ_INDEX = "QuizFragment_ARG_QUIZ_INDEX"
        const val INVALID_QUIZ_INDEX = -1
        val TAG:String = QuizFragment::class.java.simpleName

        fun newInstance(questionIndex: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putInt(ARG_QUIZ_INDEX, questionIndex)
            fragment.arguments = args
            return fragment
        }
    }
}
