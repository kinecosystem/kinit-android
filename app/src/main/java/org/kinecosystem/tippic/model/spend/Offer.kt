package org.kinecosystem.tippic.model.spend

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.kinecosystem.tippic.model.Provider
import org.kinecosystem.tippic.model.isValid

const val TYPE_P2P = "p2p"

@Parcelize
data class Offer(
        @SerializedName("id") val id: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("domain") val domain: String,
        @SerializedName("title") val title: String?,
        @SerializedName("desc") val description: String?,
        @SerializedName("image_url") val imageUrl: String?,
        @SerializedName("type_image_url") val imageTypeUrl: String?,
        @SerializedName("price") val price: Int?,
        @SerializedName("address") val address: String?,
        @SerializedName("unavailable_reason") val unavailableReason: String?,
        @SerializedName("cannot_buy_reason") val cantBuyReason: String?,
        @SerializedName("provider") val provider: Provider?) : Parcelable

fun Offer.isValid(): Boolean {
    if (id.isNullOrBlank() || title.isNullOrBlank() || description.isNullOrBlank() || type.isNullOrBlank() ||
        address.isNullOrBlank() || imageUrl.isNullOrBlank() || imageTypeUrl.isNullOrBlank() || domain.isNullOrBlank()
        || price == null || provider == null) {
        return false
    }
    return provider.isValid()
}

fun Offer.isP2p(): Boolean {
    return type.equals(TYPE_P2P)
}

fun Offer.isAvailable(): Boolean{
    return unavailableReason.isNullOrEmpty()
}

fun Offer.hasEnoughKinToBuy(): Boolean{
    return cantBuyReason.isNullOrEmpty()
}