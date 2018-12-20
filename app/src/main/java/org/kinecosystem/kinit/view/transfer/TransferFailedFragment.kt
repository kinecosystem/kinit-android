package org.kinecosystem.kinit.view.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.view.BaseFragment


class TransferFailedFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.transferring_to_app_failed_layout, container, false)
        view.findViewById<TextView>(R.id.close_text).setOnClickListener {
            if (activity is TransferActions) {
                (activity as TransferActions).onClose()
            }
        }
        return view
    }

    companion object {
        fun newInstance(): TransferFailedFragment {
            return TransferFailedFragment()
        }
    }


}
