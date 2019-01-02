package org.kinecosystem.tippic.viewmodel.earn


import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import org.kinecosystem.tippic.BuildConfig
import org.kinecosystem.tippic.TippicApplication
import org.kinecosystem.tippic.analytics.Analytics
import org.kinecosystem.tippic.analytics.Events
import org.kinecosystem.tippic.model.TaskState
import org.kinecosystem.tippic.model.earn.ChosenAnswers
import org.kinecosystem.tippic.repository.CategoriesRepository
import org.kinecosystem.tippic.repository.UserRepository
import org.kinecosystem.tippic.server.TaskService
import org.kinecosystem.tippic.util.Scheduler
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
        TippicApplication.coreComponent.inject(this)

        val task = categoriesRepository.currentTaskInProgress
        val event = Events.Business.EarningTaskCompleted(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                categoriesRepository.currentCategoryTitle,
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)

        if (categoriesRepository.shouldShowCaptcha) {
            showCaptcha(context)
        } else {
            submitAnswers()
        }
    }

    fun onResume() {
        val task = categoriesRepository.currentTaskRepo?.taskInProgress
        val event = Events.Analytics.ViewTaskEndPage(
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                categoriesRepository.currentCategoryTitle,
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)
    }

    private fun submitAnswers(token: String = "") {
        var answers = listOf<ChosenAnswers>()
        categoriesRepository.currentTaskRepo?.let {
            answers = it.getChosenAnswers()
        }
        taskService.submitQuestionnaireAnswers(
                userRepository.userInfo,
                categoriesRepository.currentTaskInProgress,
                token,
                answers
        )
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

