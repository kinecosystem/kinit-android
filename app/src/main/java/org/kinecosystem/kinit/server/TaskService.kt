package org.kinecosystem.kinit.server

import android.content.Context
import android.util.Log
import com.google.gson.JsonElement
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.isValid
import org.kinecosystem.kinit.model.earn.preload
import org.kinecosystem.kinit.model.user.UserInfo
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.api.TasksApi
import org.kinecosystem.kinit.util.GeneralUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskService(context: Context, api: TasksApi,
                  private val categoriesRepository: CategoriesRepository,
                  private val userRepo: UserRepository, private val walletService: Wallet, private val categoriesService: CategoriesService) {

    private val tasksApi: TasksApi = api
    private val applicationContext: Context = context.applicationContext
    var lastSubmissionTime: Long = -1
        private set

    fun submitQuestionnaireAnswers(
            userInfo: UserInfo,
            task: Task?,
            token: String,
            chosenAnswers: List<ChosenAnswers>) {

        if (!GeneralUtils.isConnected(applicationContext)) {
            categoriesRepository.currentTaskRepo?.taskState = TaskState.SUBMIT_ERROR_RETRY
            return
        }

        if (userInfo.publicAddress.isBlank()) {
            categoriesRepository.currentTaskRepo?.taskState = TaskState.SUBMIT_ERROR_NO_RETRY
            return
        }

        if (task == null || !task.isValid()) {
            categoriesRepository.currentTaskRepo?.taskState = TaskState.SUBMIT_ERROR_NO_RETRY
            return
        }

        categoriesRepository.currentTaskRepo?.taskState = TaskState.SUBMITTED

        val submitInfo = TasksApi.SubmitInfo(task.id.orEmpty(),
                token, chosenAnswers, userInfo.publicAddress)
        submitQuestionnaireAnswers(submitInfo)
    }

    fun retrieveTruexActivity(agent: String, callback: OperationResultCallback<JsonElement?>) {
        tasksApi.truexActivity(userRepo.userId(), agent).enqueue(object : Callback<TasksApi.TrueXResponse> {
            override fun onFailure(call: Call<TasksApi.TrueXResponse>?, t: Throwable?) {
                callback.onError(0)
            }

            override fun onResponse(call: Call<TasksApi.TrueXResponse>?, response: Response<TasksApi.TrueXResponse>?) {
                if (response != null && response.isSuccessful && response.body()?.status.equals("ok")) {
                    callback.onResult(response.body()?.activity)
                } else {
                    callback.onError(1)
                }
            }
        })
    }

    fun retrieveAllTasks(callback: OperationResultCallback<Boolean>? = null) {
        tasksApi.nextTasks(userRepo.userId()).enqueue(object : Callback<TasksApi.NextTasksResponse> {
            override fun onResponse(call: Call<TasksApi.NextTasksResponse>?,
                                    response: Response<TasksApi.NextTasksResponse>?) {

                if (response != null && response.isSuccessful) {
                    response.body()?.let {
                        it.tasksMap.let {
                            categoriesRepository.updateTasks(it)
                            //load task images
                            for (entry in it) {
                                for (task in entry.value) {
                                    task.preload(applicationContext)
                                }
                            }
                        }
                        categoriesRepository.shouldShowCaptcha = it.showCaptcha
                    } ?: run {
                        callback?.onError(ERROR_EMPTY_RESPONSE)
                    }
                }
            }

            override fun onFailure(call: Call<TasksApi.NextTasksResponse>?, t: Throwable?) {
                Log.e("TaskService", "onFailure called with throwable $t")
                callback?.onError(ERROR_APP_SERVER_FAILED_RESPONSE)
            }
        })
    }

    fun retrieveNextTask(categoryId: String, callback: OperationResultCallback<Boolean>? = null) {

        tasksApi.nextCategoryTasks(userRepo.userId(), categoryId).enqueue(object : Callback<TasksApi.NextCategoryTasksResponse> {
            override fun onResponse(call: Call<TasksApi.NextCategoryTasksResponse>?,
                                    response: Response<TasksApi.NextCategoryTasksResponse>?) {
                if (response != null && response.isSuccessful) {
                    response.body()?.let {
                        categoriesRepository.updateNextTask(categoryId, it.tasks.firstOrNull())
                        //preload images
                        it.tasks.firstOrNull()?.preload(applicationContext)
                        categoriesRepository.updateAvailableTasksCount(categoryId, it.availableTasksCount)
                        categoriesRepository.shouldShowCaptcha = it.showCaptcha
                        callback?.onResult(true)
                    } ?: run {
                        callback?.onError(ERROR_EMPTY_RESPONSE)
                    }
                }
            }

            override fun onFailure(call: Call<TasksApi.NextCategoryTasksResponse>?, t: Throwable?) {
                Log.e("TaskService", "onFailure called with throwable $t")
                callback?.onError(0)
            }
        })
    }

    private fun submitQuestionnaireAnswers(submitInfo: TasksApi.SubmitInfo) {
        lastSubmissionTime = System.currentTimeMillis()
        val currentTaskRepo = categoriesRepository.currentTaskRepo
        tasksApi.submitTaskResults(userRepo.userId(), submitInfo).enqueue(
                object : Callback<TasksApi.TaskSubmitResponse> {
                    override fun onResponse(call: Call<TasksApi.TaskSubmitResponse>?,
                                            response: Response<TasksApi.TaskSubmitResponse>?) {

                        if (response != null && response.isSuccessful) {
                            Log.d("TaskService", "submit onResponse: ${response.body()}")
                            categoriesRepository.currentTaskRepo?.taskState = TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD

                            walletService.onEarnTransactionCompleted.set(false)
                            retrieveNextTask(categoriesRepository.currentTaskInProgress?.category_id!!, object : OperationResultCallback<Boolean> {
                                override fun onResult(result: Boolean) {
                                    currentTaskRepo?.clearChosenAnswers()
                                    categoriesService.retrieveCategories()
                                }

                                override fun onError(errorCode: Int) {
                                }

                            })
                        } else {
                            Log.d("TaskService", "submit onResponse null or isSuccessful=false: $response")
                            currentTaskRepo?.taskState = TaskState.SUBMIT_ERROR_RETRY
                        }
                    }

                    override fun onFailure(call: Call<TasksApi.TaskSubmitResponse>?, t: Throwable?) {
                        Log.e("TaskService", "submit onFailure called with throwable $t")
                        currentTaskRepo?.taskState = TaskState.SUBMIT_ERROR_RETRY
                    }
                })
    }
}

