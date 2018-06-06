package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.format.DateUtils.DAY_IN_MILLIS
import org.kinecosystem.kinit.CoreComponentsProvider
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.startDateInMillis
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.network.OperationCompletionCallback
import org.kinecosystem.kinit.util.TimeUtils
import org.kinecosystem.kinit.view.TabViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val AVAILABILITY_DATE_FORMAT = "MMM dd"

class EarnViewModel(private val coreComponents: CoreComponentsProvider, private val navigator: Navigator) :
    TabViewModel {

    private val questionnaireRepo = coreComponents.questionnaireRepo()
    var shouldShowTask = ObservableBoolean()
    var shouldShowTaskNotAvailableYet = ObservableBoolean()
    var shouldShowNoTask = ObservableBoolean(false)
    var isQuestionnaireStarted = questionnaireRepo.isQuestionnaireStarted
    var nextAvailableDate: ObservableField<String> = ObservableField("")
    var isAvailableTomorrow: ObservableBoolean = ObservableBoolean(false)
    var balance: ObservableField<String> = coreComponents.services().walletService.balance
    var authorName = ObservableField<String>()
    var authorImageUrl = ObservableField<String?>()
    var title = ObservableField<String?>()
    var description = ObservableField<String?>()
    var kinReward = ObservableField<String>()
    var minToComplete = ObservableField<String>()
    private var scheduler = coreComponents.scheduler()
    private var scheduledRunnable: Runnable? = null

    init {
        refresh()
    }

    fun startQuestionnaire() {
        val task = questionnaireRepo.task
        val bEvent = Events.Business.EarningTaskStarted(
            task?.provider?.name,
            task?.minToComplete,
            task?.kinReward,
            task?.tagsString(),
            task?.id,
            task?.title,
            task?.type)
        coreComponents.analytics().logEvent(bEvent)

        val aEvent = Events.Analytics.ClickStartButtonOnTaskPage(
            isQuestionnaireStarted.get(),
            task?.provider?.name,
            task?.minToComplete,
            task?.kinReward,
            task?.tagsString(),
            task?.id,
            task?.title,
            task?.type)
        coreComponents.analytics().logEvent(aEvent)
        navigator.navigateTo(Navigator.Destination.QUESTIONNAIRE)
    }

    private fun refresh() {
        val task = coreComponents.questionnaireRepo().task
        authorName.set(task?.provider?.name)
        authorImageUrl.set(task?.provider?.imageUrl)
        title.set(task?.title)
        description.set(task?.description)
        kinReward.set(task?.kinReward?.toString())
        minToComplete.set(convertMinToCompleteToString(task?.minToComplete))
        handleAvailability()
        if (task == null) {
            coreComponents.services().taskService.retrieveNextTask(object : OperationCompletionCallback {
                override fun onError(errorCode: Int) {
                }

                override fun onSuccess() {
                    if (coreComponents.questionnaireRepo().task != null) {
                        refresh()
                    }
                }
            })
        }
    }

    private fun handleAvailability() {
        if (scheduledRunnable != null) {
            scheduler.cancel(scheduledRunnable)
        }

        if (questionnaireRepo.task == null) {
            shouldShowNoTask.set(true)
            shouldShowTask.set(false)
            shouldShowTaskNotAvailableYet.set(false)
        } else {
            val questionnaireAvailable = isQuestionnaireAvailable()
            shouldShowTask.set(questionnaireAvailable)
            shouldShowTaskNotAvailableYet.set(!questionnaireAvailable)
            shouldShowNoTask.set(false)

            if (!shouldShowTask.get()) {

                nextAvailableDate.set(nextAvailableDate())
                isAvailableTomorrow.set(isAvailableTomorrow())
                if (isAvailableTomorrow.get()) {
                    val diff = questionnaireRepo.task?.startDateInMillis()!! - scheduler.currentTimeMillis()
                    scheduledRunnable = Runnable {
                        shouldShowTask.set(true)
                        shouldShowTaskNotAvailableYet.set(false)
                    }
                    scheduler.scheduleOnMain(scheduledRunnable, diff)
                } else {
                    scheduledRunnable = Runnable { handleAvailability() }
                    scheduler.scheduleOnMain(scheduledRunnable, DAY_IN_MILLIS)
                }
            }
        }
    }

    private fun isQuestionnaireAvailable(): Boolean {
        val taskDate: Long = questionnaireRepo.task?.startDateInMillis() ?: scheduler.currentTimeMillis()
        return scheduler.currentTimeMillis() >= taskDate
    }

    private fun isAvailableTomorrow(): Boolean {
        return timeToUnlockInDays(questionnaireRepo.task) == 1
    }

    private fun timeToUnlockInDays(task: Task?): Int {
        val millisAtNextMidnight = TimeUtils.millisAtNextMidnight(scheduler.currentTimeMillis())
        val startDate = task?.startDateInMillis() ?: scheduler.currentTimeMillis()

        return (1 + ((startDate - millisAtNextMidnight) / DAY_IN_MILLIS)).toInt()
    }

    fun nextAvailableDate(): String {
        val dateInMillis = questionnaireRepo.task?.startDateInMillis() ?: 0
        return SimpleDateFormat(AVAILABILITY_DATE_FORMAT).format(Date(dateInMillis))
    }

    override fun onScreenVisibleToUser() {
        refresh()
        if (shouldShowTask.get()) {
            onEarnScreenVisible()
        } else if (shouldShowNoTask.get()) {
            onNoTasksAvailableVisible()
        } else {
            onLockedScreenVisible()
        }
    }

    private fun onEarnScreenVisible() {
        val task = coreComponents.questionnaireRepo().task
        val event = Events.Analytics.ViewTaskPage(task?.provider?.name,
            task?.minToComplete,
            task?.kinReward,
            task?.tagsString(),
            task?.id,
            task?.title,
            task?.type)
        coreComponents.analytics().logEvent(event)
    }

    private fun onLockedScreenVisible() {
        val questionnaire = questionnaireRepo.task
        val timeToUnlockInDays = timeToUnlockInDays(questionnaire)

        val event = Events.Analytics.ViewLockedTaskPage(timeToUnlockInDays)
        coreComponents.analytics().logEvent(event)
    }

    private fun onNoTasksAvailableVisible() {
        coreComponents.analytics().logEvent(Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN))
    }

    private fun convertMinToCompleteToString(minToComplete: Float?): String =
        when {
            minToComplete == null -> "0"
            (minToComplete * 10).toInt() % 10 == 0 -> minToComplete.toInt().toString()
            else -> minToComplete.toString()
        }
}

