package org.kinecosystem.kinit.server.api

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.Task
import retrofit2.Call
import retrofit2.http.*

interface TasksApi {

    data class SubmitInfo(
        @SerializedName("id") val taskId: String,
        @SerializedName("captcha_token") val token: String,
        @SerializedName("results") val chosenAnswersList: List<ChosenAnswers>,
        @SerializedName("address") val publicAddress: String)

    data class TaskSubmitResponse(@SerializedName("tx_id") val transactionId: String)

    data class TrueXResponse(
        @SerializedName("status") val status: String,
        @SerializedName("activity") val activity: JsonElement)

    @POST("/user/task/results")
    fun submitTaskResults(
        @Header(USER_HEADER_KEY) userId: String,
        @Body submitInfo: SubmitInfo): Call<TaskSubmitResponse>

    data class NextTasksResponse(@SerializedName("tasks") val taskList: List<Task>,
        @SerializedName("show_captcha") val showCaptcha: Boolean,
        @SerializedName("tz") val declaredTimeZone: String)

    @GET("/user/tasks")
    fun nextTasks(@Header(USER_HEADER_KEY) userId: String): Call<NextTasksResponse>


    @GET("/truex/activity")
    fun truexActivity(@Header(USER_HEADER_KEY) userId: String, @Query("user-agent") agent: String): Call<TrueXResponse>
}

