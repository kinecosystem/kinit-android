package org.kinecosystem.kinit.model.earn

import com.google.gson.annotations.SerializedName

data class ChosenAnswer(
    @SerializedName("qid") val questionId: String,
    @SerializedName("aid") val answerId: String)

