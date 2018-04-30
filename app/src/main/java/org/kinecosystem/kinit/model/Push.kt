package org.kinecosystem.kinit.model

import com.google.gson.annotations.SerializedName

class Push {

    /*
        Class used by KinMessagingService
        The push notification data that we receive from our servers is
        in the following format:
        "data"={"push_id":"the_id",
                "push_type":"tx_completed/engage-recent"
                "message":"NotificationMessage/TransactionCompleteMessage"}
     */

    companion object {
        const val TYPE_TX_COMPLETED = "tx_completed"
        const val TYPE_ENGAGEMENT = "engage-recent"
        const val ID_DATA_KEY = "push_id"
        const val TYPE_DATA_KEY = "push_type"
        const val MESSAGE_DATA_KEY = "message"
    }

    data class NotificationMessage(
        @SerializedName("body")
        val body: String? = null,
        @SerializedName("title")
        val title: String? = null)


    data class TransactionCompleteMessage(
        @SerializedName("kin")
        val kin: Int? = null,
        @SerializedName("user_id")
        val userId: String? = null,
        @SerializedName("task_id")
        val taskId: String? = null,
        @SerializedName("tx_hash")
        val txHash: String? = null,
        @SerializedName("type")
        val type: String? = null)
}
