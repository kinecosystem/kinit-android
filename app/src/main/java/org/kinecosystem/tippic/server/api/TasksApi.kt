package org.kinecosystem.tippic.server.api

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.kinecosystem.tippic.model.earn.ChosenAnswers
import org.kinecosystem.tippic.model.earn.Task
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

    data class NextTasksResponse(@SerializedName("tasks") val tasksMap: Map<String, List<Task>>,
                                 @SerializedName("show_captcha") val showCaptcha: Boolean = false,
                                 @SerializedName("tz") val declaredTimeZone: String = "")

    data class NextCategoryTasksResponse(@SerializedName("tasks") val tasks: List<Task>,
                                         @SerializedName("available_tasks_count") val availableTasksCount: Int = 0,
                                         @SerializedName("show_captcha") val showCaptcha: Boolean = false)

    @GET("/user/tasks")
    fun nextTasks(@Header(USER_HEADER_KEY) userId: String): Call<NextTasksResponse>

    @GET("/user/category/{id}/tasks")
    fun nextCategoryTasks(@Header(USER_HEADER_KEY) userId: String, @Path("id") categoryId: String): Call<NextCategoryTasksResponse>


    @GET("/truex/activity")
    fun truexActivity(@Header(USER_HEADER_KEY) userId: String, @Query("user-agent") agent: String): Call<TrueXResponse>
}

