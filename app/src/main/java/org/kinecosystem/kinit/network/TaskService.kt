package org.kinecosystem.kinit.network

import android.content.Context
import android.util.Log
import com.google.gson.JsonElement
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.isValid
import org.kinecosystem.kinit.model.user.UserInfo
import org.kinecosystem.kinit.repository.TasksRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskService(context: Context, api: TasksApi,
                  val questionnaireRepo: TasksRepository,
                  private val userId: String, private val walletService: Wallet) {

    private val questionnaireApi: TasksApi = api
    private val applicationContext: Context = context.applicationContext

    fun submitQuestionnaireAnswers(
        userInfo: UserInfo,
        task: Task?,
        chosenAnswers: List<ChosenAnswers>) {

        if (!NetworkUtils.isConnected(applicationContext)) {
            questionnaireRepo.taskState = TaskState.SUBMIT_ERROR_RETRY
            return
        }

        if (userInfo.publicAddress.isBlank()) {
            questionnaireRepo.taskState = TaskState.SUBMIT_ERROR_NO_RETRY
            return
        }

        if (task == null || !task.isValid()) {
            questionnaireRepo.taskState = TaskState.SUBMIT_ERROR_NO_RETRY
            return
        }

        val submitInfo = TasksApi.SubmitInfo(task.id.orEmpty(),
            chosenAnswers, userInfo.publicAddress)
        submitQuestionnaireAnswers(submitInfo)
    }

    fun getTrueXTask(callback: OperationResultCallback<JsonElement?>) {
        questionnaireApi.truexActivity(userId).enqueue(object : Callback<TasksApi.TrueXResponse> {
            override fun onFailure(call: Call<TasksApi.TrueXResponse>?, t: Throwable?) {
                callback.onError(0)
            }

            override fun onResponse(call: Call<TasksApi.TrueXResponse>?, response: Response<TasksApi.TrueXResponse>?) {
                if (response != null && response.isSuccessful && response.body()?.status.equals("ok")) {
                    callback.onResult(response?.body()?.activity)
                }else{
                    callback.onError(1)
                    Log.e("###", "### getTrueX return not successful response")
                }
            }
        })
    }

    fun retrieveNextTask(callback: OperationCompletionCallback? = null) {

        questionnaireApi.nextTasks(userId).enqueue(object : Callback<TasksApi.NextTasksResponse> {
            override fun onResponse(call: Call<TasksApi.NextTasksResponse>?,
                response: Response<TasksApi.NextTasksResponse>?) {

                if (response != null && response.isSuccessful) {
                    Log.d("TaskService", "onResponse: ${response.body()}")
                    val taskResponse = response.body()
                    val taskList: List<Task> = taskResponse?.taskList.orEmpty()
                    var task: Task? = if (taskList.isNotEmpty() && taskList[0].isValid()) taskList[0] else null
                    questionnaireRepo.replaceQuestionnaire(task, applicationContext)
                    callback?.onSuccess()
                } else {
                    questionnaireRepo.replaceQuestionnaire(null, applicationContext)
                    Log.d("TaskService", "onResponse null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }
            }

            override fun onFailure(call: Call<TasksApi.NextTasksResponse>?, t: Throwable?) {
                Log.d("TaskService", "onFailure called with throwable $t")
                questionnaireRepo.replaceQuestionnaire(null, applicationContext)
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    private fun submitQuestionnaireAnswers(submitInfo: TasksApi.SubmitInfo) {
        questionnaireApi.submitTaskResults(userId, submitInfo).enqueue(
            object : Callback<TasksApi.TaskSubmitResponse> {
                override fun onResponse(call: Call<TasksApi.TaskSubmitResponse>?,
                    response: Response<TasksApi.TaskSubmitResponse>?) {

                    if (response != null && response.isSuccessful) {
                        Log.d("TaskService", "onResponse: ${response.body()}")
                        questionnaireRepo.taskState = TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD

                        walletService.onEarnTransactionCompleted.set(false)
                        retrieveNextTask()
                    } else {
                        Log.d("TaskService", "onResponse null or isSuccessful=false: $response")
                        questionnaireRepo.taskState = TaskState.SUBMIT_ERROR_RETRY
                    }
                }

                override fun onFailure(call: Call<TasksApi.TaskSubmitResponse>?, t: Throwable?) {
                    Log.d("TaskService", "onFailure called with throwable $t")
                    questionnaireRepo.taskState = TaskState.SUBMIT_ERROR_RETRY
                }
            })
    }
}

