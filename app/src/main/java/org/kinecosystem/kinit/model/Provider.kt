package org.kinecosystem.kinit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Provider(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null) : Parcelable

fun Provider.isValid(): Boolean {
    return !name.isNullOrBlank() && !imageUrl.isNullOrBlank()
}
