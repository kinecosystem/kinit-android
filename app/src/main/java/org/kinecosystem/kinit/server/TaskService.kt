package org.kinecosystem.kinit.server

import android.content.Context
import android.util.Log
import com.google.gson.JsonElement
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.isValid
import org.kinecosystem.kinit.model.user.UserInfo
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.TasksApi
import org.kinecosystem.kinit.util.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskService(context: Context, api: TasksApi,
                  val tasksRepo: TasksRepository,
                  private val userRepo: UserRepository, private val walletService: Wallet) {

    private val tasksApi: TasksApi = api
    private val applicationContext: Context = context.applicationContext

    fun submitQuestionnaireAnswers(
            userInfo: UserInfo,
            task: Task?,
            chosenAnswers: List<ChosenAnswers>) {

        if (!GeneralUtils.isConnected(applicationContext)) {
            tasksRepo.taskState = TaskState.SUBMIT_ERROR_RETRY
            return
        }

        if (userInfo.publicAddress.isBlank()) {
            tasksRepo.taskState = TaskState.SUBMIT_ERROR_NO_RETRY
            return
        }

        if (task == null || !task.isValid()) {
            tasksRepo.taskState = TaskState.SUBMIT_ERROR_NO_RETRY
            return
        }

        tasksRepo.taskState = TaskState.SUBMITTED

        val submitInfo = TasksApi.SubmitInfo(task.id.orEmpty(),
                chosenAnswers, userInfo.publicAddress)
        submitQuestionnaireAnswers(submitInfo)
    }

    fun retrieveTruexActivity(agent: String, callback: OperationResultCallback<JsonElement?>) {
        tasksApi.truexActivity(userRepo.userId(), agent).enqueue(object : Callback<TasksApi.TrueXResponse> {
            override fun onFailure(call: Call<TasksApi.TrueXResponse>?, t: Throwable?) {
                callback.onError(0)
            }

            override fun onResponse(call: Call<TasksApi.TrueXResponse>?, response: Response<TasksApi.TrueXResponse>?) {
                if (response != null && response.isSuccessful && response.body()?.status.equals("ok")) {
                    callback.onResult(response?.body()?.activity)
                } else {
                    callback.onError(1)
                    Log.e("###", "### getTrueX return not successful response")
                }
            }
        })
    }

    fun retrieveNextTask(callback: OperationResultCallback<Boolean>? = null) {

        tasksApi.nextTasks(userRepo.userId()).enqueue(object : Callback<TasksApi.NextTasksResponse> {
            override fun onResponse(call: Call<TasksApi.NextTasksResponse>?,
                                    response: Response<TasksApi.NextTasksResponse>?) {

                if (response != null && response.isSuccessful &&
                        tasksRepo.taskState != TaskState.IN_PROGRESS && tasksRepo.taskState != TaskState.SUBMITTED) {
                    Log.d("TaskService", "onResponse: ${response.body()}")
                    val taskResponse = response.body()
                    val taskList: List<Task> = taskResponse?.taskList.orEmpty()

                    var task: Task? = if (taskList.isNotEmpty() && taskList[0].isValid()) taskList[0] else null
                    if (hasChanged(task)) {
                        tasksRepo.replaceTask(task, applicationContext)
                        callback?.onResult(true)
                    } else callback?.onResult(false)
                } else {
                    Log.d("TaskService", "onResponse null or isSuccessful=false: $response")
                    callback?.onError(ERROR_EMPTY_RESPONSE)
                }
            }

            override fun onFailure(call: Call<TasksApi.NextTasksResponse>?, t: Throwable?) {
                Log.d("TaskService", "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    private fun hasChanged(task: Task?): Boolean {

        tasksRepo.task?.let {
            return task != tasksRepo.task
        }

        // previous task was null
        return task != null
    }

    private fun submitQuestionnaireAnswers(submitInfo: TasksApi.SubmitInfo) {
        tasksApi.submitTaskResults(userRepo.userId(), submitInfo).enqueue(
                object : Callback<TasksApi.TaskSubmitResponse> {
                    override fun onResponse(call: Call<TasksApi.TaskSubmitResponse>?,
                                            response: Response<TasksApi.TaskSubmitResponse>?) {

                        if (response != null && response.isSuccessful) {
                            Log.d("TaskService", "onResponse: ${response.body()}")
                            tasksRepo.taskState = TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD

                            walletService.onEarnTransactionCompleted.set(false)
                            retrieveNextTask()
                        } else {
                            Log.d("TaskService", "onResponse null or isSuccessful=false: $response")
                            tasksRepo.taskState = TaskState.SUBMIT_ERROR_RETRY
                        }
                    }

                    override fun onFailure(call: Call<TasksApi.TaskSubmitResponse>?, t: Throwable?) {
                        Log.d("TaskService", "onFailure called with throwable $t")
                        tasksRepo.taskState = TaskState.SUBMIT_ERROR_RETRY
                    }
                })
    }
}

