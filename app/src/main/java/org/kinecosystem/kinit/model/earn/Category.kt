package org.kinecosystem.kinit.model.earn

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Category(
        @SerializedName("id") val id: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("title") val title: String?,
        @SerializedName("desc") val description: String?,
        @SerializedName("category_image_url") val imageUrl: String?,
        @SerializedName("header_image_url") val headerImageUrl: String?,
        @SerializedName("color") val color: Int?,
        @SerializedName("task") var task: Task? = null)


fun Category.isValid(): Boolean {
    if (id.isNullOrBlank() || title.isNullOrBlank() || description.isNullOrBlank() || type.isNullOrBlank() || imageUrl.isNullOrBlank()) {
        return false
    }
    return true
}

fun Category.hasTask():Boolean {
    return task != null // && task.isValid()
}

fun Category.setTask(newTask: Task?){
    task = newTask
}