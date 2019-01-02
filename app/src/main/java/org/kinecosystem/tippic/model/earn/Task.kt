package org.kinecosystem.tippic.model.earn

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils.DAY_IN_MILLIS
import com.google.gson.annotations.SerializedName
import org.kinecosystem.tippic.model.Provider
import org.kinecosystem.tippic.model.isValid
import org.kinecosystem.tippic.util.ImageUtils
import org.kinecosystem.tippic.util.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

const val TASK_TYPE_QUESTIONNAIRE: String = "questionnaire"
const val TASK_TYPE_QUIZ: String = "quiz"
const val TASK_TYPE_TRUEX: String = "truex"
private const val AVAILABILITY_DATE_FORMAT = "MMM dd"


data class Task(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("memo")
        val memo: String? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("desc")
        val description: String? = null,
        @SerializedName("price")
        val kinReward: Int? = null,
        @SerializedName("start_date")
        val startDateInSeconds: Long? = null,
        @SerializedName("min_to_complete")
        val minToComplete: Float? = null,
        @SerializedName("cat_id")
        val category_id: String? = null,
        @SerializedName("tags")
        val tags: List<String>? = null,
        @SerializedName("post_task_actions")
        val postTaskActions: List<PostTaskAction>? = null,
        @SerializedName("provider")
        val provider: Provider? = null,
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("updated_at")
        val lastUpdatedAt: Long? = null,
        @SerializedName("items")
        val questions: List<Question>? = null)

fun Task.isValid(): Boolean {
    if (id.isNullOrBlank() || title.isNullOrBlank() || description.isNullOrBlank() || type.isNullOrBlank() || memo.isNullOrBlank() || category_id.isNullOrBlank() ||
            kinReward == null || minToComplete == null || provider == null || questions == null || startDateInSeconds == null || lastUpdatedAt == null) {
        return false
    }
    if (isQuiz()) {
        if (questions.isEmpty() || questions.any { question -> question.quiz_data == null })
            return false
    }
    if (!questions.orEmpty().all { question -> question.isValid() }) {
        return false
    }
    return provider.isValid()
}

fun Task.isQuestionnaire(): Boolean = type.equals(TASK_TYPE_QUESTIONNAIRE)

fun Task.isQuiz(): Boolean = type.equals(TASK_TYPE_QUIZ)

fun Task.hasPostActions(): Boolean = postTaskActions != null && postTaskActions.isNotEmpty()

fun Task.isTaskWebView(): Boolean = type.equals(TASK_TYPE_TRUEX)

fun Task.startDateInMillis(): Long? {
    return startDateInSeconds?.times(1000)
}

fun Task.isAvailableNow(currentTimeInMillis: Long): Boolean {
    startDateInMillis()?.let {
        return currentTimeInMillis >= it
    }
    return false
}

fun Task.isAvailableTomorrow(currentTimeInMillis: Long): Boolean {
    return timeToUnlockInDays(currentTimeInMillis) == 1
}

private fun Task.timeToUnlockInDays(currentTimeInMillis: Long): Int {
    val millisAtNextMidnight = TimeUtils.millisAtNextMidnight(currentTimeInMillis)
    val startDate = startDateInMillis() ?: currentTimeInMillis
    return (1 + ((startDate - millisAtNextMidnight) / DAY_IN_MILLIS)).toInt()
}

fun Task.nextAvailableDate(): String {
    startDateInMillis()?.let {
        return SimpleDateFormat(AVAILABILITY_DATE_FORMAT, Locale.US).format(Date(it))
    }
    return ""
}

fun Task.preload(context: Context) {
    if (hasPostActions()) {
        val url = postTaskActions?.first()?.iconUrl

        if (!TextUtils.isEmpty(url)) {
            ImageUtils.fetchImage(context.applicationContext, url)
        }
    }
    for (question: Question in questions.orEmpty()) {
        if (question.hasImages()) {
            ImageUtils.fetchImages(context.applicationContext, question.getImagesUrls())
        }
    }

}




