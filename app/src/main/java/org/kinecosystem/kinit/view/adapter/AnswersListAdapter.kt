package org.kinecosystem.kinit.view.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import org.kinecosystem.kinit.BR
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.model.earn.Question
import org.kinecosystem.kinit.viewmodel.earn.QuestionViewModel

class AnswersListAdapter(val questionModel: QuestionViewModel) : RecyclerView.Adapter<AnswersListAdapter.ViewHolder>() {

    private val answersState = Array(itemCount, { false })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersListAdapter.ViewHolder {

        val model = AnswerViewModel(questionModel.questionType)
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, model.layoutId, parent, false)
        return AnswersListAdapter.ViewHolder(binding, model)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer: Answer? = questionModel.answers?.get(position)
        holder.bind(answer, answersState[position])
        holder.itemView.setOnClickListener {
            answersState[position] = questionModel.onAnswer.answerSelected(answer)
            notifyItemChanged(position)
            it?.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.bounce))
        }
    }

    override fun getItemCount(): Int {
        return questionModel.answers?.size ?: 0
    }

    interface OnAnswerSelectedListener {
        fun answerSelected(answer: Answer?): Boolean
    }

    class ViewHolder(val binding: ViewDataBinding, val model: AnswerViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(answer: Answer?, isSelected: Boolean) {
            model.text = answer?.text
            model.imageUrl = answer?.imageUrl
            model.isSelected = isSelected
            binding.setVariable(BR.model, model)
            binding.executePendingBindings()
        }
    }
}

class AnswerViewModel(questionType: String) {
    val isTextAlignedLeft = questionType == Question.QuestionType.TEXT_EMOJI.type
    val isMultipleAnswers = questionType == Question.QuestionType.TEXT_MULTIPLE.type

    var text: String? = null
        set(value) {
            field = value
            isTextPresent = !value.isNullOrBlank()
        }
    var imageUrl: String? = null
    var isSelected: Boolean = false
    var isTextPresent: Boolean = false
        private set

    val layoutId = when (questionType) {
        Question.QuestionType.TEXT_IMAGE.type -> R.layout.image_text_answer_layout
        Question.QuestionType.TEXT_MULTIPLE.type -> R.layout.multiple_answers_button
        else -> R.layout.text_answer_layout
    }
}