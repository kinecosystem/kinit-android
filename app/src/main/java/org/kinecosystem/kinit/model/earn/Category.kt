package org.kinecosystem.kinit.model.earn

import com.google.gson.annotations.SerializedName


data class Category(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("image")
        val image: String? = null,
        @SerializedName("header_image")
        val headerImage: String? = null,
        @SerializedName("color")
        val color: Int? = null,
        @SerializedName("tasks")
        val tasks: List<Task>? = null)

fun Category.isValid(): Boolean {
    if (id.isNullOrBlank() || title.isNullOrBlank() || image.isNullOrBlank() || headerImage.isNullOrBlank() || tasks == null) {
        return false
    }
    return true
}

fun Category.isEnabled(): Boolean = tasks != null && tasks.isNotEmpty()
