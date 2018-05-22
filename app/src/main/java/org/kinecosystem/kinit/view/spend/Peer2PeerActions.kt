package org.kinecosystem.kinit.view.spend

interface Peer2PeerActions {

    fun pickContact()
    fun requestReadContactsPermission()
    fun onContactParseError()

    fun closeScreen()

    fun showDialog(resTitle: Int, resMessage: Int, resAction: Int, shouldFinish: Boolean, logErrorType: String? = null)

}