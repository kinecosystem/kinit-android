package org.kinecosystem.kinit.model

import com.google.gson.annotations.SerializedName

data class Provider(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null)

fun Provider.isValid(): Boolean {
    return !name.isNullOrBlank() && !imageUrl.isNullOrBlank()
}
