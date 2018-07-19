package org.kinecosystem.kinit.view.customView

import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.quiz_answer_layout.view.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.earn.Answer
import org.kinecosystem.kinit.util.Scheduler
import java.util.*
import javax.inject.Inject


class QuizAnswerView : ConstraintLayout {

    interface OnSelectionListener {
        fun onAnimComplete()
        fun onAnswered(answer: Answer?)
    }

    @Inject
    lateinit var scheduler: Scheduler

    private val DELAY_AFTER_ANIM = 1500L
    private val MAX_PARTICLES = 40
    private val MIN_ANIM_PARTICLE_DURATION = 3000L
    private val frameLayoutParam: FrameLayout.LayoutParams by lazy {
        val param = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        param.gravity = Gravity.CENTER
        param
    }
    private val particlesRes by lazy {
        listOf(R.drawable.big_purple_circle, R.drawable.big_rectangle_turkiz, R.drawable.big_yellow_triangle, R.drawable.small_purple_circle, R.drawable.small_rectangle_turkiz, R.drawable.small_yellow_triangle)
    }
    private val rand by lazy { Random() }


    lateinit var listener: OnSelectionListener
    var isCorrect: Boolean = false
    var reward: Int = 0
        set(value) {
            reward_text.text = "+$value KIN"
        }

    var answer: Answer? = null
        set(value) {
            answer_text.text = value?.text
            answer_feedback.text = value?.text
            field = value
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        KinitApplication.coreComponent.inject(this)
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.quiz_answer_layout, this, true)
        setOnClickListener {
            listener.onAnswered(answer)
            if (isCorrect) {
                showCorrect(true)
            } else {
                showWrong()
            }
        }
    }

    fun showCorrect(userClicked: Boolean = false) {
        if (isCorrect) {
            answer_feedback.background = resources.getDrawable(R.drawable.answer_bg_correct)
            if (userClicked) {
                //make the view last in parent
                val container: ViewGroup = parent as ViewGroup
                container.removeView(this)
                container.addView(this)
                animateBackground()
                animateParticles()
                animateReward()
            } else {
                animateBackground(true)
            }
        }
    }

    private fun animateReward() {
        reward_text.y = reward_text.height.toFloat()
        reward_text.alpha = 1f
        reward_text.animate().setStartDelay(1100).translationY(0f).setInterpolator(OvershootInterpolator(3f)).setDuration(600).withEndAction {
            scheduler.scheduleOnMain({ listener.onAnimComplete() }, DELAY_AFTER_ANIM)
        }
    }

    private fun animateParticles() {
        for (i in 1..MAX_PARTICLES) {
            animateParticle(addParticle())
        }
    }

    private fun addParticle(): View {
        val particle = ImageView(context)
        particle.setImageResource(particlesRes[(particlesRes.size * rand.nextFloat()).toInt()])
        frameLayoutParam.gravity = Gravity.CENTER
        particle.layoutParams = frameLayoutParam
        particle.scaleType = ImageView.ScaleType.CENTER
        frame_container.addView(particle)
        return particle
    }

    private fun animateParticle(view: View) {
        view.animate().translationYBy(width * rand.nextFloat() * if (rand.nextBoolean()) 1 else -1).translationXBy(width * rand.nextFloat() * if (rand.nextBoolean()) 1 else -1).rotation(rand.nextFloat()).setDuration(MIN_ANIM_PARTICLE_DURATION + 1000 * rand.nextFloat().toLong()).alpha(0f).setInterpolator(AccelerateDecelerateInterpolator())
    }

    private fun showWrong() {
        animateBackground()
    }

    private fun animateBackground(notifyAnimationEnd: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim = ViewAnimationUtils.createCircularReveal(answer_feedback, answer_feedback.width / 2, answer_feedback.height / 2, 0f, answer_feedback.width.toFloat())
            anim.doOnEnd {
                if (notifyAnimationEnd) {
                    scheduler.scheduleOnMain({ listener.onAnimComplete() }, DELAY_AFTER_ANIM)
                }
            }
            answer_feedback.alpha = 1f
            anim.start()
        } else {
            answer_feedback.animate().alpha(1f).setDuration(250).withEndAction {
                if (notifyAnimationEnd) {
                    scheduler.scheduleOnMain({ listener.onAnimComplete() }, DELAY_AFTER_ANIM)
                }
            }
        }
    }
}
