package org.kinecosystem.kinit.view.transfer

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.transfer_request_layout.view.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.view.BaseActivity
import javax.inject.Inject


const val ACTION = "kin.transfer.response"

class TransferLauncherActivity : BaseActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    var sourcePkg = "kinit"
    var sourceClass = "some class"

    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_request_layout)
        intent?.let {

            if (it.hasExtra("sourcePkg")) {
                sourcePkg = intent.extras.getString("sourcePkg")
                findViewById<TextView>(R.id.pkg).text = sourcePkg
            }


            if (it.hasExtra("sourceClass")) {
                sourceClass = intent.extras.getString("sourceClass")
                findViewById<TextView>(R.id.classt).text = sourceClass
            }


            //findViewById<TextView>(R.id.info_text).text = "got intent $intent secret $secret  "
            //findViewById<TextView>(R.id.publickey).text = "public key ${userRepository.userInfo.publicAddress}"
            findViewById<Button>(R.id.button).setOnClickListener({
                onClickBack()
            })

            //test no check request
            //onClickBack()
        }
    }

    fun onClickBack() {
        val intent = Intent(ACTION)
        intent.component = ComponentName(sourcePkg, sourceClass)
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("source", application.packageName)
        //need to return hashed value of the kin public key with the key of kinit reside in the server
        intent.putExtra("walletPublicKey", userRepository.userInfo.publicAddress)

        //intent.resolveActivity(packageManager)
        try {

            startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "cant start activity", Toast.LENGTH_LONG).show()
        }


        finish()
    }

    companion object {

        fun getIntent(context: Context): Intent {
            return Intent(context, TransferLauncherActivity::class.java)
        }
    }

}