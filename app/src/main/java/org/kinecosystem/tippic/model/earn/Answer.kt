package org.kinecosystem.tippic.model.earn

import com.google.gson.annotations.SerializedName

data class Answer(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null)

fun Answer.isValid(): Boolean = !id.isNullOrBlank() && (!text.isNullOrBlank() || !imageUrl.isNullOrBlank())

