package org.kinecosystem.tippic.view.backup

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.backup_complete.*
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.view.BaseFragment

const val START_DELAY = 450L
const val END_DELAY = 1250L

class BackupCompleteFragment : BaseFragment() {
    companion object {
        fun newInstance(): Fragment = BackupCompleteFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lockerAnimation.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                lockerAnimation.postDelayed({
                    activity?.finish()
                }, END_DELAY)
            }

        })
        lockerAnimation.postDelayed({
            lockerAnimation?.playAnimation()
        }, START_DELAY)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.backup_complete, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            (it as BackupActions).getBackUpModel().onResumeFragment()
        }
    }
}