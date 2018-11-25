package org.kinecosystem.kinit.model.spend

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EcoApplication(
        @SerializedName("identifier") val identifier: String,
        @SerializedName("name") val name: String,
        @SerializedName("transfer_data") val transferData: TransferData?,
        @SerializedName("meta_data") val data: EcoApplicationData) : Parcelable

@Parcelize
data class TransferData(
        @SerializedName("launch_activity") val identifier: String,
        @SerializedName("launch_action") val transferData: String) : Parcelable

@Parcelize
data class EcoApplicationData(
        @SerializedName("short_description") val descriptionShort: String,
        @SerializedName("card_image_url") val cardImageUrl: String,
        @SerializedName("icon_url") val iconUrl: String,
        @SerializedName("category_name") val categoryTitle: String,
        @SerializedName("app_url") val appUrl: String,
        @SerializedName("image_url_0") val appImage0Url: String,
        @SerializedName("image_url_1") val appImage1Url: String?,
        @SerializedName("image_url_2") val appImage2Url: String?,
        @SerializedName("kin_usage") val kinUsage: String,
        @SerializedName("description") val description: String) : Parcelable

fun EcoApplication.isKinTransferSupported() = transferData != null

fun EcoApplication.getHeaderImageCount(): Int {
    return when {
        !data.appImage2Url.isNullOrEmpty() && !data.appImage1Url.isNullOrEmpty() && !data.appImage0Url.isNullOrEmpty() -> 3
        !data.appImage1Url.isNullOrEmpty() && !data.appImage0Url.isNullOrEmpty() -> 2
        !data.appImage0Url.isEmpty() -> 1
        else -> 0
    }
}

fun EcoApplication.getHeaderImageUrl(position: Int): String {
    return when (position) {
        2 -> data.appImage2Url.orEmpty()
        1 -> data.appImage1Url.orEmpty()
        else -> data.appImage0Url
    }
}