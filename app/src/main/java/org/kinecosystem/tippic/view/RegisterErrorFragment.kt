package org.kinecosystem.tippic.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.kinecosystem.tippic.R

class RegisterErrorFragment : BaseFragment() {

    companion object {
        val TAG = RegisterErrorFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): RegisterErrorFragment {
            return RegisterErrorFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.register_error_layout, container, false)
        view.findViewById<TextView>(R.id.close).setOnClickListener {activity?.finish()}
        return view
    }

}