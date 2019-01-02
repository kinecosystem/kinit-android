package org.kinecosystem.tippic.model.earn

import com.google.gson.annotations.SerializedName

data class PostTaskAction(
        @SerializedName("title")
        val title: String,
        @SerializedName("text")
        val text: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("text_positive")
        val positiveText: String,
        @SerializedName("text_negative")
        val negativeText: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("icon_url")
        val iconUrl: String,
        @SerializedName("action_name")
        val actionName: String)