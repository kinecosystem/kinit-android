package org.kinecosystem.kinit.viewmodel.spend

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.model.spend.EcoApplication
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.NetworkServices
import org.kinecosystem.kinit.view.transfer.TransferActions
import javax.inject.Inject

class TransferToAppViewModel(private val navigator: Navigator, val app: EcoApplication, val transferActions: TransferActions?) {

    val appIcon = app.data.iconUrl
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var services: NetworkServices

    var amount = ObservableInt(0)
    var sendEnabled = ObservableBoolean(false)
    lateinit var balance:ObservableField<String>

    var positiveAmount = ObservableBoolean(false)

        var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            if (editable.length >= 2) {
                if (editable[0] == '0') {
                    editable.delete(0, 1)
                }
            }
            positiveAmount.set(editable.length > 0 && !editable.toString().startsWith("0"))
            amount.set(if (editable.length > 0) Integer.valueOf(editable.toString()) else 0)
            sendEnabled.set(amount.get()>0 && amount.get() <= balance.get().toInt())
        }
    }


    init {
        KinitApplication.coreComponent.inject(this)
        balance = services.walletService.balance
    }

    fun onClose(view: View?) {
        navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
    }

    fun onSend(view: View?) {
        transferActions?.onStartTransferring(amount.get())
    }
}
