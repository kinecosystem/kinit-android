package org.kinecosystem.kinit.view.customView

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.single_security_answer_layout.view.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.server.api.BackupApi
import org.kinecosystem.kinit.viewmodel.walletBoot.RestoreWalletViewModel

class SecurityQuestionAnswerView: ConstraintLayout {
    private var backupQuestion: BackupApi.BackUpQuestion? = null
    private var index: Int = 0
    private lateinit var model: RestoreWalletViewModel

    init {
        KinitApplication.coreComponent.inject(this)
    }

    constructor(context: Context, model: RestoreWalletViewModel, questionIndex: Int, backupQuestionId: Int) : super(context) {
        this.model = model
        this.backupQuestion = model.getHintQuestionById(backupQuestionId)
        this.index = questionIndex
        init(context)
    }

    fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.single_security_answer_layout, this, true)
        qid.text = context.getString(R.string.question_block_string).format(index + 1)
        q_text.text = backupQuestion?.hint
        userInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                model.setAnswer(index, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
}