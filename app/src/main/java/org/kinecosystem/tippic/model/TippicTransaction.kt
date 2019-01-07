package org.kinecosystem.tippic.model

import com.google.gson.annotations.SerializedName

data class TxInfo(
    @SerializedName("memo")
    val memo: String? = null,
    @SerializedName("task_id")
    val id: String? = null
)

fun TxInfo.isValid(): Boolean {
    return !(memo.isNullOrBlank() || id.isNullOrBlank())
}

data class TippicTransaction(
    @SerializedName("tx_hash")
    val txHash: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("desc")
    val description: String? = null,
    @SerializedName("amount")
    val amount: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("client_received")
    val clientReceived: Boolean? = null,
    @SerializedName("date")
    val date: Long? = null,
    @SerializedName("provider")
    val provider: Provider? = null,
    @SerializedName("tx_info")
    val txInfo: TxInfo? = null,
    var txBalance: Int? = null
)

fun TippicTransaction.isValid(): Boolean {
    if (txHash.isNullOrBlank() || txHash.isNullOrBlank() || amount == null ||
        type.isNullOrBlank() || clientReceived == null || date == null ||
        provider == null || txInfo == null) {
        return false
    }
    return provider.isValid() && txInfo.isValid()
}