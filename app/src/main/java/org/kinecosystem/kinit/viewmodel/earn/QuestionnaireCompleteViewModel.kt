package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.TaskState
import org.kinecosystem.kinit.model.earn.tagsString
class QuestionnaireCompleteViewModel(coreComponents: CoreComponentsProvider) {

    private val questionnaireRepo = coreComponents.questionnaireRepo()
    private val userRepo = coreComponents.userRepo()
    private val services = coreComponents.services()
    private val analytics = coreComponents.analytics()
    var submitComplete: Boolean = false
        get() = (questionnaireRepo.taskState == TaskState.SUBMITTED_SUCCESS_WAIT_FOR_REWARD || questionnaireRepo.taskState == TaskState.TRANSACTION_COMPLETED)

    init {
        submitAnswers()
    }

    private fun submitAnswers() {
        val task = questionnaireRepo.task
        val event = Events.Business.EarningTaskCompleted(task?.provider?.name,
            task?.minToComplete,
            task?.kinReward,
            task?.tagsString(),
            task?.id,
            task?.title,
            task?.type)
        analytics.logEvent(event)

        services.taskService.submitQuestionnaireAnswers(
            userRepo.userInfo,
            task,
            questionnaireRepo.getChosenAnswers())

    }

    fun onResume() {
        val task = questionnaireRepo.task
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
