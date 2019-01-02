package org.kinecosystem.tippic.view.backup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.server.api.BackupApi

class BackupQuestionSpinnerAdapter(val context: Context?, private val hints: List<BackupApi.BackUpQuestion>) : BaseAdapter() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)


    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        val view = inflater.inflate(R.layout.backup_question_spinner_item_layout, null)
        view.findViewById<TextView>(R.id.question).text = hints[index].hint
        return view
    }

    override fun getItem(index: Int): Any {
        return hints[index]
    }

    override fun getItemId(index: Int): Long {
        return hints[index].id.toLong()
    }

    override fun getCount(): Int {
        return hints.size
    }

}