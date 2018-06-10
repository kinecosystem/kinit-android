package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.network.ServicesProvider
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.repository.UserRepository
import javax.inject.Inject

class QuestionnaireCompleteViewModel() {


    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var questionnaireRepository: QuestionnaireRepository

    var submitComplete: Boolean = false
        get() = (questionnaireRepository.taskState == TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD || questionnaireRepository.taskState == TaskState.TRANSACTION_COMPLETED)

    init {
        KinitApplication.coreComponent.inject(this)
        submitAnswers()
    }

    private fun submitAnswers() {
        val task = questionnaireRepository.task
        val event = Events.Business.EarningTaskCompleted(task?.provider?.name,
            task?.minToComplete,
            task?.kinReward,
            task?.tagsString(),
            task?.id,
            task?.title,
            task?.type)
        analytics.logEvent(event)

        servicesProvider.taskService.submitQuestionnaireAnswers(
            userRepository.userInfo,
            task,
            questionnaireRepository.getChosenAnswers())

    }

    fun onResume() {
        val task = questionnaireRepository.task
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
}
