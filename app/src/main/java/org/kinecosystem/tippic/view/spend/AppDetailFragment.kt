package org.kinecosystem.tippic.view.spend

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.databinding.AppDetailsLayoutBinding
import org.kinecosystem.tippic.model.spend.EcoApplication
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.util.BindingUtils
import org.kinecosystem.tippic.util.GeneralUtils
import org.kinecosystem.tippic.view.BaseFragment
import org.kinecosystem.tippic.viewmodel.spend.AppViewModel

class AppDetailFragment : BaseFragment() {

    lateinit var model: AppViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<AppDetailsLayoutBinding>(inflater, R.layout.app_details_layout, container, false)
        arguments?.let { args ->
            if (args.containsKey(ARG_APP)) {
                val app = args.getParcelable<EcoApplication>(ARG_APP)
                activity?.let { activity ->
                    model = AppViewModel(Navigator(activity), app)
                    binding.model = model
                    binding.viewPager.adapter = AppPagerAdapter(activity.supportFragmentManager, model)
                    binding.viewPager.addOnPageChangeListener(model)
                    val headerImageHeight = resources.getDimension(R.dimen.app_header_image_long_height)
                    binding.content.viewTreeObserver.addOnGlobalLayoutListener {
                        val screenHeight = GeneralUtils.getScreenHeight(activity)
                        //have long scroll more then the image height
                        if (binding.content.height > screenHeight + headerImageHeight) {
                            binding.scrollview.viewTreeObserver.addOnScrollChangedListener {
                                var alpha = 0f
                                if (binding.scrollview.scrollY >= headerImageHeight * .7f) {
                                    alpha = (binding.scrollview.scrollY - headerImageHeight * .7f) / (headerImageHeight * .3f)
                                }
                                binding.topPanel.alpha = alpha
                                binding.topPanelAppIcon.alpha = alpha
                                binding.topPanelBtn.alpha = alpha
                                binding.topPanelCloseX.alpha = alpha
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
    }

    companion object {
        val ARG_APP = "arg_app"
        val TAG = AppDetailFragment::class.java.simpleName

        fun newInstance(app: EcoApplication): AppDetailFragment {
            val fragment = AppDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_APP, app)
            fragment.arguments = args
            return fragment
        }
    }

    private class AppPagerAdapter(fragmentManager: FragmentManager, val model: AppViewModel) : FragmentPagerAdapter(fragmentManager) {

        override fun getCount(): Int {
            return model.headerImagesCount
        }

        override fun getItem(position: Int): Fragment? {
            return AppImageFragment.newInstance(model.getHeaderImageUrl(position))
        }
    }
}

internal class AppImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: ImageView = inflater.inflate(R.layout.image_fragment_layout, container, false) as ImageView
        val imageUrl = arguments?.getString(ARG_PARAM_IMAGE_URL, "")
        BindingUtils.loadImage(view, imageUrl)
        return view
    }

    companion object {
        const val ARG_PARAM_IMAGE_URL = "ARG_PARAM_IMAGE_URL"
        fun newInstance(imageUrl: String): AppImageFragment {
            val fragment = AppImageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_IMAGE_URL, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }
}

