package org.kinecosystem.kinit.viewmodel.earn


import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import org.kinecosystem.kinit.BuildConfig
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.repository.UserRepository
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import java.util.concurrent.Executor
import javax.inject.Inject

class QuestionnaireCompleteViewModel {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var categoriesRepository: CategoriesRepository
    @Inject
    lateinit var taskService: TaskService
    @Inject
    lateinit var scheduler: Scheduler


    constructor(context: Context) {
        KinitApplication.coreComponent.inject(this)

<<<<<<< HEAD
        val task = taskRepository.taskInProgress
=======
        val task = categoriesRepository.currentTaskInProgress
>>>>>>> taskcategories
        val event = Events.Business.EarningTaskCompleted(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)

        if(categoriesRepository.shouldShowCaptcha){
            showCaptcha(context)
        } else {
            submitAnswers()
        }
    }

    fun onResume() {
<<<<<<< HEAD
        val task = taskRepository.taskInProgress
=======
        val task = categoriesRepository.currentTaskRepo?.taskInProgress
>>>>>>> taskcategories
        val event = Events.Analytics.ViewTaskEndPage(
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)
    }

    private fun submitAnswers(token: String = "") {
        taskService.submitQuestionnaireAnswers(
<<<<<<< HEAD
            userRepository.userInfo,
            taskRepository.taskInProgress,
            token,
            taskRepository.getChosenAnswers())
=======
                userRepository.userInfo,
                categoriesRepository.currentTaskInProgress,
                token,
                categoriesRepository.getChosenAnswers()
                )
>>>>>>> taskcategories
    }

    private fun showCaptcha(context: Context) {
        analytics.logEvent(Events.Analytics.ViewCaptchaPopup())
        categoriesRepository.currentTaskRepo?.taskState = TaskState.SHOWING_CAPTCHA
        var executor = Executor { command -> scheduler.post(command) }

        SafetyNet.getClient(context).verifyWithRecaptcha(BuildConfig.CaptchaApiSecret)
                .addOnSuccessListener(executor,
                        OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse> { captchaResponse ->
                            submitAnswers(token = captchaResponse?.tokenResult.orEmpty())
                        })
                .addOnFailureListener(executor, OnFailureListener { exception ->
                    val code = if (exception is ApiException) {
                        exception.statusCode
                    } else -1
                    analytics.logEvent(Events.BILog.CaptchaFailed("$exception $code ${exception.message}"))
                    submitAnswers(token = "")
                })
                .addOnCanceledListener(executor, OnCanceledListener {
                    analytics.logEvent(Events.BILog.CaptchaFailed("Cancelled"))
                    submitAnswers(token = "")
                })
    }
}

