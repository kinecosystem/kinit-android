package org.kinecosystem.kinit.view.transfer

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.databinding.AppsConnectionsLayoutBinding
import org.kinecosystem.kinit.model.spend.EcosystemApp
import org.kinecosystem.kinit.view.BaseFragment
import org.kinecosystem.kinit.view.spend.AppDetailFragment
import org.kinecosystem.kinit.viewmodel.spend.AppsConnectionViewModel

class AppsConnectionFragment : BaseFragment() {

    lateinit var model: AppsConnectionViewModel
    var transferActions: TransferActions? = null
    lateinit var binding: AppsConnectionsLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<AppsConnectionsLayoutBinding>(inflater, R.layout.apps_connections_layout, container, false)

        transferActions = activity as TransferActions
        arguments?.let { args ->
            if (args.containsKey(AppDetailFragment.ARG_APP)) {
                val app = args.getParcelable<EcosystemApp>(AppsConnectionFragment.ARG_APP)
                context?.let {
                    model = AppsConnectionViewModel(app)
                    binding.model = model
                }
            }
        }
        return binding.root
    }

    fun animateConnected() {
        val wiggle = AnimationUtils.loadAnimation(activity, R.anim.wiggle_connection)
        binding.progressBar.rotation = 180f
        wiggle.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                transferActions?.onAnimationConnectionEnd()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
        binding.appIcon.startAnimation(wiggle)
    }

    companion object {
        val ARG_APP = "arg_app"
        val TAG: String = AppsConnectionFragment::class.java.simpleName

        fun newInstance(app: EcosystemApp): AppsConnectionFragment {
            val fragment = AppsConnectionFragment()
            val args = Bundle()
            args.putParcelable(ARG_APP, app)
            fragment.arguments = args
            return fragment
        }
    }


}
