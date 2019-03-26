package org.kinecosystem.kinit.view.transfer


import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil.setContentView
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kin.ecosystem.transfer.AccountInfoManager
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.viewmodel.AccountInfoViewModel

class AccountInfoActivity : AppCompatActivity() {

    lateinit var model: AccountInfoViewModel

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AccountInfoActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<org.kinecosystem.kinit.databinding.KinecosystemActivityAccountInfoBinding>(this, R.layout.kinecosystem_activity_account_info)
        model = AccountInfoViewModel(AccountInfoManager(this), intent)

        model.onClose.addOnPropertyChangedCallback(
                object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        if (sender is ObservableBoolean && sender.get()) {
                            finish()
                        }
                    }
                })
        binding.model = model
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
    }

    override fun onPause() {
        super.onPause()
        model.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        model.onBackPressed()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        model.onDetach()
    }

}