package org.kinecosystem.kinit.model.spend

import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.Provider
import org.kinecosystem.kinit.model.isValid

data class Coupon (
        @SerializedName("offer_id") val id: String?,
        @SerializedName("title") val title: String?,
        @SerializedName("desc") val desc: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("value") val value: String?,
        @SerializedName("date") val dateInSeconds: Long?,
        @SerializedName("provider") val provider: Provider?
)

fun Coupon.isValid() : Boolean{
    if (id.isNullOrBlank() || title.isNullOrBlank() || desc.isNullOrBlank()
            || type.isNullOrBlank() || value.isNullOrBlank() || dateInSeconds == null
            || provider == null){
        return false
    }
    return provider.isValid()
}