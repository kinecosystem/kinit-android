package org.kinecosystem.kinit.view.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import org.kinecosystem.kinit.R


class PagesIndicatorsView : LinearLayout {

    lateinit var container: LinearLayout

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.pages_indicators_layout, this, true)
        container = view.findViewById(R.id.container)
        setPageSelected(0)
    }

    fun addCircles(count: Int) {
        if (count > 2) {
            for (i in 1..count - 2) {
                addCircle()
            }
        }
    }

    private fun addCircle() {
        val newView = View(context)
        newView.setBackgroundResource(R.drawable.page_circle)
        val param = LinearLayout.LayoutParams(resources.getDimension(R.dimen.page_indicator_size).toInt(), resources.getDimension(R.dimen.page_indicator_size).toInt())
        param.setMargins(resources.getDimension(R.dimen.page_indicator_margin).toInt(), 0, 0, 0)
        newView.layoutParams = param
        container.addView(newView)
    }

    fun setPageSelected(pageIndex: Int) {
        for (childIndex in 0..container.childCount) {
            container.getChildAt(childIndex)?.isSelected = childIndex == pageIndex
        }
    }
}

