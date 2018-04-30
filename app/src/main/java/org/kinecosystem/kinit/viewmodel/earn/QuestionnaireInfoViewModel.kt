package org.kinecosystem.kinit.viewmodel.earn

import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.navigation.Navigator

class QuestionnaireInfoViewModel(coreComponents: CoreComponentsProvider, private val navigator: Navigator) {
    val task: Task? = coreComponents.questionnaireRepo().task
    private val analytics = coreComponents.analytics()

    var authorName: String? = ""
        get() = task?.provider?.name
    var authorImageUrl: String? = ""
        get() = task?.provider?.imageUrl
    var title: String? = ""
        get() = task?.title
    var description: String? = ""
        get() = task?.description
    var kinReward: Int? = 0
        get() = task?.kinReward
    var minToComplete: Float? = 0f
        get() = task?.minToComplete

    fun onResume() {
        val event = Events.Analytics.ViewTaskPage(task?.provider?.name,
            task?.minToComplete,
            task?.kinReward,
            task?.tagsString(),
            task?.id,
            task?.title,
            task?.type)
        analytics.logEvent(event)
    }

    fun onStartQuestionnaire() {
        navigator.navigateTo(Navigator.Destination.QUESTIONNAIRE)
    }

}
