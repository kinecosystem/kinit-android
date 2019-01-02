package org.kinecosystem.tippic.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("picture_id")
    val picture_id: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("image_url")
    val image_url: String? = null,
    @SerializedName("author")
    val author: Author? = null,
    @SerializedName("tips_sum")
    val tips_sum: Int = 0
)

data class Author(
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("user_id")
        val user_id: String? = null,
        @SerializedName("public_address")
        val public_address: String? = null
)


fun Author.isValid(): Boolean {
    return !name.isNullOrBlank() && !user_id.isNullOrBlank() && !public_address.isNullOrBlank()
}

fun Picture.isValid(): Boolean {
    return author != null && author.isValid() and !title.isNullOrBlank() and !image_url.isNullOrBlank()
}
