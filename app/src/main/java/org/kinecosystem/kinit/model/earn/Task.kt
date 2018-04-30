package org.kinecosystem.kinit.model.earn

import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.Provider
import org.kinecosystem.kinit.model.isValid

data class Task(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("desc")
    val description: String? = null,
    @SerializedName("price")
    val kinReward: Int? = null,
    @SerializedName("start_date")
    val startDateInSeconds: Long? = 0,
    @SerializedName("min_to_complete")
    val minToComplete: Float? = null,
    @SerializedName("tags")
    val tags: List<String>? = null,
    @SerializedName("provider")
    val provider: Provider? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("items")
    val questions: List<Question>? = null)

fun Task.isValid(): Boolean {
    if (id.isNullOrBlank() || title.isNullOrBlank() || description.isNullOrBlank() || type.isNullOrBlank() ||
        kinReward == null || minToComplete == null || provider == null || questions == null || startDateInSeconds == null) {
        return false
    }
    if (!questions.orEmpty().all { question -> question.isValid() }) {
        return false
    }
    return provider.isValid()
}

fun Task.tagsString(): String? {
    return tags?.joinToString(", ")
}

fun Task.startDateInMillis(): Long? {
    return startDateInSeconds?.times(1000)
}
