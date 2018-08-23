package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.server.ServicesProvider
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.repository.UserRepository
import javax.inject.Inject

class QuestionnaireCompleteViewModel {

    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var servicesProvider: ServicesProvider
    @Inject
    lateinit var taskRepository: TasksRepository

    init {
        KinitApplication.coreComponent.inject(this)
        submitAnswers()
    }

    private fun submitAnswers() {
        val task = taskRepository.task
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
            taskRepository.getChosenAnswers())

    }

    fun onResume() {
        val task = taskRepository.task
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
