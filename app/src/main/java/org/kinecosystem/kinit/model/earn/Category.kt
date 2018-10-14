package org.kinecosystem.kinit.model.earn

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.kinecosystem.kinit.util.ImageUtils

data class Category(
        @SerializedName("id") val id: String,
        @SerializedName("available_tasks_count") var availableTasksCount: Int = 0,
        @SerializedName("title") val title: String?,
        @SerializedName("ui_data") val uiData: UiData?) {
}


@Parcelize
data class UiData(@SerializedName("color") val color: String?,
                  @SerializedName("header_image_url") val headerImageUrl: String?,
                  @SerializedName("image_url") val imageUrl: String?) : Parcelable

@Parcelize
data class HeaderMessage(@SerializedName("title") val title: String,
                         @SerializedName("subtitle") val subtitle: String) : Parcelable

fun Category.isValid(): Boolean {
    if (id.isNullOrBlank() || title.isNullOrBlank() || uiData == null) {
        return false
    }
    return uiData.isValid()
}

fun UiData.isValid(): Boolean {
    if (color.isNullOrBlank() || headerImageUrl.isNullOrBlank() || imageUrl.isNullOrBlank()) {
        return false
    }
    return true
}

fun Category.isEnabled(): Boolean = availableTasksCount > 0

fun Category.getBgColor(): Int {
    uiData?.let {
        it.color?.let {
            return Color.parseColor(it)
        }
    }
    return Color.WHITE
}

fun Category.preload(context: Context) {
    uiData?.imageUrl?.let {
        ImageUtils.fetchImage(context.applicationContext, it)
    }
    uiData?.headerImageUrl?.let {
        ImageUtils.fetchImage(context.applicationContext, it)
    }
}
