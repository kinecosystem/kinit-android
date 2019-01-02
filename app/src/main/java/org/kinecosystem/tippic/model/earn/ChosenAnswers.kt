package org.kinecosystem.tippic.model.earn

import com.google.gson.annotations.SerializedName

data class ChosenAnswers(
    @SerializedName("qid") val questionId: String,
    @SerializedName("aid") val answersIds: List<String>)

