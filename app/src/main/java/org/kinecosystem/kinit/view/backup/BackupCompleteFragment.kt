package org.kinecosystem.kinit.view.backup

import android.animation.Animator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_complete.*
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.BaseFragment

const val START_DELAY = 450L
const val END_DELAY = 1250L

class BackupCompleteFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupCompleteFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lockerAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                lockerAnimation.postDelayed({
                    activity?.finish()
                }, END_DELAY)
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

        })
        lockerAnimation.postDelayed({ lockerAnimation.playAnimation() }, START_DELAY)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.backup_complete, container, false)
    }
}