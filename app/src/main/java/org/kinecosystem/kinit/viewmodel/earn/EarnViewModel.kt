package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.format.DateUtils.DAY_IN_MILLIS
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.blockchain.Wallet
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.isQuiz
import org.kinecosystem.kinit.model.earn.startDateInMillis
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.util.TimeUtils
import org.kinecosystem.kinit.view.TabViewModel
import org.kinecosystem.kinit.viewmodel.backup.BackupAlertManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val AVAILABILITY_DATE_FORMAT = "MMM dd"

class EarnViewModel(private val backupAlertManager: BackupAlertManager?, private val navigator: Navigator) : TabViewModel {

    @Inject
    lateinit var tasksRepository: TasksRepository
    @Inject
    lateinit var walletService: Wallet
    @Inject
    lateinit var taskService: TaskService
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var analytics: Analytics

    var shouldShowTask = ObservableBoolean()
    var shouldShowTaskNotAvailableYet = ObservableBoolean()
    var shouldShowNoTask = ObservableBoolean(false)

    var nextAvailableDate: ObservableField<String> = ObservableField("")
    var isAvailableTomorrow: ObservableBoolean = ObservableBoolean(false)
    var authorName = ObservableField<String>()
    var authorImageUrl = ObservableField<String?>()
    var title = ObservableField<String?>()
    var description = ObservableField<String?>()
    var kinReward = ObservableField<String>()
    var minToComplete = ObservableField<String>()
    var isQuiz = ObservableBoolean(false)

    var balance: ObservableField<String>
    var isTaskStarted: ObservableBoolean

    private var scheduledRunnable: Runnable? = null

    init {
        KinitApplication.coreComponent.inject(this)
        balance = walletService.balance
        isTaskStarted = tasksRepository.isTaskStarted
        refresh()
    }

    fun startTask() {
        tasksRepository.onTaskStarted()
        val task = tasksRepository.task
        val bEvent = Events.Business.EarningTaskStarted(
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(bEvent)

        val aEvent = Events.Analytics.ClickStartButtonOnTaskPage(
                isTaskStarted.get(),
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(aEvent)
        navigator.navigateTo(Navigator.Destination.TASK)
    }

    private fun refresh() {
        tasksRepository.task?.let { task ->
            isQuiz.set(task.isQuiz())
            authorName.set(task.provider?.name)
            authorImageUrl.set(task.provider?.imageUrl)
            title.set(task.title)
            description.set(task.description)
            kinReward.set(if (task.isQuiz()) {
                var totalReward = 0
                (task.questions?.forEach { question ->
                    totalReward += (question.quiz_data?.reward ?: 0)
                })
                totalReward += task.kinReward ?: 0
                totalReward.toString()
            } else task.kinReward?.toString())
            minToComplete.set(convertMinToCompleteToString(task.minToComplete))
        }
        handleAvailability()
    }

    private fun handleAvailability() {
        if (scheduledRunnable != null) {
            scheduler.cancel(scheduledRunnable)
        }

        if (tasksRepository.task == null) {
            shouldShowNoTask.set(true)
            shouldShowTask.set(false)
            shouldShowTaskNotAvailableYet.set(false)
            backupAlertManager?.showNagAlertIfNeeded()
        } else {
            val taskAvailable = tasksRepository.isTaskAvailable()
            shouldShowTask.set(taskAvailable)
            shouldShowTaskNotAvailableYet.set(!taskAvailable)
            shouldShowNoTask.set(false)
            if (!taskAvailable) {
                backupAlertManager?.showNagAlertIfNeeded()
            }

            if (!shouldShowTask.get()) {
                nextAvailableDate.set(nextAvailableDate())
                isAvailableTomorrow.set(isAvailableTomorrow())
                if (isAvailableTomorrow.get()) {
                    val diff = tasksRepository.task?.startDateInMillis()!! - scheduler.currentTimeMillis()
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

    private fun isAvailableTomorrow(): Boolean {
        return timeToUnlockInDays(tasksRepository.task) == 1
    }

    private fun timeToUnlockInDays(task: Task?): Int {
        val millisAtNextMidnight = TimeUtils.millisAtNextMidnight(scheduler.currentTimeMillis())
        val startDate = task?.startDateInMillis() ?: scheduler.currentTimeMillis()

        return (1 + ((startDate - millisAtNextMidnight) / DAY_IN_MILLIS)).toInt()
    }

    private fun nextAvailableDate(): String {
        tasksRepository.task?.startDateInMillis()?.let {
            return SimpleDateFormat(AVAILABILITY_DATE_FORMAT, Locale.US).format(Date(it))
        }
        return ""
    }

    override fun onScreenVisibleToUser() {
        refresh()
        when {
            shouldShowTask.get() -> onEarnScreenVisible()
            shouldShowNoTask.get() -> onNoTasksAvailableVisible()
            else -> onLockedScreenVisible()
        }
        checkForUpdates()
    }

    private fun checkForUpdates() {
        taskService.retrieveNextTask(object : OperationResultCallback<Boolean> {
            override fun onError(errorCode: Int) {
            }

            override fun onResult(taskHasChanged: Boolean) {
                if (taskHasChanged) {
                    refresh()
                }
            }
        })
    }

    private fun onEarnScreenVisible() {
        val task = tasksRepository.task
        val event = Events.Analytics.ViewTaskPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                task?.tagsString(),
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)
    }

    private fun onLockedScreenVisible() {
        val task = tasksRepository.task
        val timeToUnlockInDays = timeToUnlockInDays(task)

        val event = Events.Analytics.ViewLockedTaskPage(timeToUnlockInDays)
        analytics.logEvent(event)
    }

    private fun onNoTasksAvailableVisible() {
        analytics.logEvent(Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN))
    }

    private fun convertMinToCompleteToString(minToComplete: Float?): String =
            when {
                minToComplete == null -> "0"
                (minToComplete * 10).toInt() % 10 == 0 -> minToComplete.toInt().toString()
                else -> minToComplete.toString()
            }
}

