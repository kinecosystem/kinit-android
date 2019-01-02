package org.kinecosystem.tippic.view.spend

interface Peer2PeerActions {

    fun pickContact()
    fun onContactParseError()
    fun onReadyForTransaction()
    fun closeScreen()
    fun onStartTransaction()
    fun onTransactionComplete()

    fun showDialog(resTitle: Int, resMessage: Int, resAction: Int, shouldShowContacts: Boolean)
    fun showDialog(resTitle: Int, resMessage: Int, messageParam: Int, resAction: Int, showContacts: Boolean)


}