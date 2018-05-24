package org.kinecosystem.kinit.viewmodel.balance

import android.content.Intent
import android.support.v7.widget.RecyclerView
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.model.spend.Coupon
import org.kinecosystem.kinit.view.adapter.CouponsListAdapter

class CouponViewModel(val coupon: Coupon, private val position: Int,
                      val parent: RecyclerView, val isExpanded: Boolean) {

    fun onClick() {
        (parent.adapter as CouponsListAdapter).expandCollapse(isExpanded, position)
    }

    fun share() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, coupon.value)
        sendIntent.type = "text/plain"
        parent.context.startActivity(Intent.createChooser(sendIntent, parent.context.resources.getText(R.string.share_code_to)))
    }
}