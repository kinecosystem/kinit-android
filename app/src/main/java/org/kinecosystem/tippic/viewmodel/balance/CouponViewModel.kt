package org.kinecosystem.tippic.viewmodel.balance

import android.content.Intent
import android.support.v7.widget.RecyclerView
import org.kinecosystem.tippic.R
import org.kinecosystem.tippic.model.spend.Coupon
import org.kinecosystem.tippic.view.adapter.CouponsListAdapter

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
        parent.context.startActivity(
            Intent.createChooser(sendIntent, parent.context.resources.getText(R.string.share_code_to)))
    }
}