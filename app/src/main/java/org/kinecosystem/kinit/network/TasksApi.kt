package org.kinecosystem.kinit.network

import com.google.gson.annotations.SerializedName
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TasksApi {

    data class SubmitInfo(
            @SerializedName("id") val taskId: String,
            @SerializedName("results") val chosenAnswersList: List<ChosenAnswers>,
            @SerializedName("address") val publicAddress: String)

    data class TaskSubmitResponse(@SerializedName("tx_id") val transactionId: String)

    @POST("/user/task/results")
    fun submitTaskResults(
        @Header(USER_HEADER_KEY) userId: String,
        @Body submitInfo: SubmitInfo): Call<TaskSubmitResponse>

    data class NextTasksResponse(@SerializedName("tasks") val taskList: List<Task>)

    @GET("/user/tasks")
    fun nextTasks(@Header(USER_HEADER_KEY) userId: String): Call<NextTasksResponse>
}