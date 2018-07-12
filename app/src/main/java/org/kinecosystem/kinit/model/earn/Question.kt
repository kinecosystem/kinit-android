package org.kinecosystem.kinit.model.earn

import com.google.gson.annotations.SerializedName

data class Question(val id: String? = null,
                    @SerializedName("text")
                    val text: String? = null,
                    @SerializedName("type")
                    val type: String,
                    @SerializedName("image_url")
                    val image_url: String? = null,
                    @SerializedName("results")
                    val answers: List<Answer>? = null) {

    enum class QuestionType(val type: String) {
        TEXT("text"),
        TEXT_IMAGE("textimage"),
        TEXT_DUAL_IMAGE("dual_image"),
        TEXT_EMOJI("textemoji"),
        TEXT_MULTIPLE("textmultiple");
    }
}

fun Question.isValid(): Boolean {
    if (id.isNullOrBlank() || text.isNullOrBlank() || type.isNullOrBlank() || answers.orEmpty().isEmpty()) {
        return false
    }
    return answers.orEmpty().all { answer -> answer.isValid() }
}

fun Question.isTypeDualImage() = type == Question.QuestionType.TEXT_DUAL_IMAGE.type

fun Question.hasImages() = type == Question.QuestionType.TEXT_IMAGE.type || type == Question.QuestionType.TEXT_DUAL_IMAGE.type

fun Question.getImagesUrls(): List<String?>? {
    var urls = answers?.filter { !it.imageUrl.isNullOrEmpty() }?.map { it.imageUrl }?.toMutableList()
    if (!image_url.isNullOrEmpty()) {
        urls?.add(image_url)
    }
    return urls
}