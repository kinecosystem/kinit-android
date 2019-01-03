package org.kinecosystem.tippic.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("picture_id")
    val picture_id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("image_url")
    val image_url: String,
    @SerializedName("author")
    val author: Author,
    @SerializedName("tips_sum")
    val tips_sum: Int = 0
)

data class Author(
        @SerializedName("name")
        val name: String,
        @SerializedName("user_id")
        val user_id: String,
        @SerializedName("public_address")
        val public_address: String
)


fun Author.isValid(): Boolean {
    return !name.isBlank() && !user_id.isBlank() && !public_address.isBlank()
}

fun Picture.isValid(): Boolean {
    return author.isValid() and !title.isBlank() and !image_url.isBlank()
}
