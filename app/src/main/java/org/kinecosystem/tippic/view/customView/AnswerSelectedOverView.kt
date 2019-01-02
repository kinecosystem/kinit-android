package org.kinecosystem.tippic.view.customView

import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewAnimationUtils
import androidx.core.animation.doOnEnd
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.model.earn.Answer


class AnswerSelectedOverView : ConstraintLayout {

    interface OnSelectionListener {
        fun onSelected(answer: Answer)
        fun onAnimComplete()
    }

    var listener: OnSelectionListener? = null
    lateinit var answer: Answer

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.answer_image_selected_view, this, true)
        setOnTouchListener { _, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> reveal(event?.x.toInt(), event?.y.toInt())
            }
            true
        }
    }

    private fun reveal(x: Int, y: Int) {
        if (x < 0 || y < 0 || y > height || !isClickable) return
        setOnTouchListener(null)
        listener?.onSelected(answer)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0f, Math.max(width, height).toFloat())
            alpha = 1f
            anim.doOnEnd {
                listener?.onAnimComplete()
            }
            anim.start()
        } else {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
            animate().alpha(1f).setDuration(shortAnimTime.toLong()).withEndAction {
                listener?.onAnimComplete()
            }
        }
    }
}
