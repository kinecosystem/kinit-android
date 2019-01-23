package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.format.DateUtils.DAY_IN_MILLIS
import android.util.Log
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.*
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.server.OperationResultCallback
import org.kinecosystem.kinit.server.TaskService
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.util.TimeUtils
import javax.inject.Inject

class CategoryTaskViewModel(private val navigator: Navigator, private val category: Category) {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    @Inject
    lateinit var taskService: TaskService

    var headerTitle = ObservableField<String>()
    var headerImage = ObservableField<String>()
    var bgColor = ObservableField<String>("#047cfc")
    var tasksCount = ObservableInt()
    var taskCountVisibility = ObservableBoolean(false)
    var shouldShowTask = ObservableBoolean(true)
    var shouldShowTaskNotAvailableYet = ObservableBoolean(false)
    var shouldShowNoTask = ObservableBoolean(false)

    var nextAvailableDate: ObservableField<String> = ObservableField("")
    var isAvailableTomorrow: ObservableBoolean = ObservableBoolean(false)
    var authorName = ObservableField<String>("author")
    var authorImageUrl = ObservableField<String?>()
    var title = ObservableField<String?>()
    var description = ObservableField<String?>()
    var kinReward = ObservableField<String>()
    var minToComplete = ObservableField<String>()
    var isQuiz = ObservableBoolean(false)
    var isTaskStarted = ObservableBoolean(false)
    var availableTasksCount = ObservableInt()
    var shouldFinishActivity = ObservableBoolean(false)
    private var task: Task?


    private var scheduledRunnable: Runnable = Runnable {
        onResume()
    }

    init {
        KinitApplication.coreComponent.inject(this)
        task = categoriesRepository.getTask(category.id)
        availableTasksCount = categoriesRepository.currentAvailableTasks
        Log.d("CategoryTaskViewModel", "got task $task")
        bindCategoryData()
        refresh()
    }

    fun refreshTaskData() {
        scheduler.cancel(scheduledRunnable)
        task = categoriesRepository.getTask(category.id)
        Log.d("CategoryTaskViewModel", "${task?.startDateInSeconds} ${scheduler.currentTimeMillis()}")
        task?.let {
            when {
                it.isAvailableNow(scheduler.currentTimeMillis()) -> {
                    shouldShowTask.set(true)
                    shouldShowNoTask.set(false)
                    shouldShowTaskNotAvailableYet.set(false)
                    bindAvailableTaskData()
                }
                it.isAvailableTomorrow(scheduler.currentTimeMillis()) -> {
                    shouldShowTask.set(false)
                    shouldShowNoTask.set(false)
                    shouldShowTaskNotAvailableYet.set(true)
                    isAvailableTomorrow.set(true)
                    val diff = it.startDateInMillis()?.minus(scheduler.currentTimeMillis())
                    diff?.let {
                        scheduler.scheduleOnMain(scheduledRunnable, it)
                    }
                }
                else -> {
                    shouldShowNoTask.set(false)
                    shouldShowTask.set(false)
                    shouldShowTaskNotAvailableYet.set(true)
                    nextAvailableDate.set(it.nextAvailableDate())
                    scheduler.scheduleOnMain(scheduledRunnable, DAY_IN_MILLIS)
                }
            }
        } ?: run {
            shouldShowNoTask.set(true)
            shouldShowTask.set(false)
            scheduler.scheduleOnMain(scheduledRunnable, DAY_IN_MILLIS)
        }
    }

    fun startTask() {
        categoriesRepository.onTaskStarted()
        val bEvent = Events.Business.EarningTaskStarted(
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                category.title,
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(bEvent)

        val aEvent = Events.Analytics.ClickStartButtonOnTaskPage(
                isTaskStarted.get(),
                task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                category.title,
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(aEvent)
        navigator.navigateToTask(category.id)
        shouldFinishActivity.set(true)
    }

    private fun bindAvailableTaskData() {
        categoriesRepository.currentTaskRepo?.let {
            isTaskStarted = it.isTaskStarted
        }
        task?.let {
            isQuiz.set(it.isQuiz())
            authorName.set(it.provider?.name)
            authorImageUrl.set(it.provider?.imageUrl)
            title.set(it.title)
            description.set(it.description)
            kinReward.set(if (it.isQuiz()) {
                var totalReward = 0
                (it.questions?.forEach { question ->
                    totalReward += (question.quiz_data?.reward ?: 0)
                })
                totalReward += it.kinReward ?: 0
                totalReward.toString()
            } else it.kinReward?.toString())
            minToComplete.set(convertMinToCompleteToString(it.minToComplete))
        }
    }

    private fun bindCategoryData() {
        headerTitle.set(category.title)
        bgColor.set(category.uiData?.color)
        category.uiData?.let {
            headerImage.set(it.headerImageUrl)
        }
        updateTaskCount()
    }

    fun onBackPressed() {
        navigator.navigateTo(Navigator.Destination.MAIN_SCREEN, true, true)
    }

    private fun updateTaskCount() {
        taskCountVisibility.set(availableTasksCount.get() > 1)
        tasksCount.set(availableTasksCount.get())
    }

    fun onResume() {
        refresh()
        when {
            shouldShowTask.get() -> onEarnScreenVisible()
            shouldShowNoTask.get() -> onNoTasksAvailableVisible()
            else -> onLockedScreenVisible()
        }
        requestTask()
    }

    fun refresh() {
        updateTaskCount()
        refreshTaskData()
    }

    private fun requestTask() {
        taskService.retrieveNextTask(category.id, object : OperationResultCallback<Boolean> {
            override fun onResult(result: Boolean) {
                refresh()
            }

            override fun onError(errorCode: Int) {
                Log.e("CategoryTaskViewModel", "error loading task")
            }
        })
    }


    private fun convertMinToCompleteToString(minToComplete: Float?): String =
            when {
                minToComplete == null -> "0"
                (minToComplete * 10).toInt() % 10 == 0 -> minToComplete.toInt().toString()
                else -> minToComplete.toString()
            }

    private fun onEarnScreenVisible() {
        val event = Events.Analytics.ViewTaskPage(task?.provider?.name,
                task?.minToComplete,
                task?.kinReward,
                category.title,
                task?.id,
                task?.title,
                task?.type)
        analytics.logEvent(event)
    }

    private fun onLockedScreenVisible() {
        val timeToUnlockInDays = timeToUnlockInDays(task)
        val event = Events.Analytics.ViewLockedTaskPage(timeToUnlockInDays)
        analytics.logEvent(event)
    }

    private fun onNoTasksAvailableVisible() {
        analytics.logEvent(Events.Analytics.ViewEmptyStatePage(Analytics.MENU_ITEM_NAME_EARN, category.title))
    }

    private fun timeToUnlockInDays(task: Task?): Int {
        val millisAtNextMidnight = TimeUtils.millisAtNextMidnight(scheduler.currentTimeMillis())
        val startDate = task?.startDateInMillis() ?: scheduler.currentTimeMillis()

        return (1 + ((startDate - millisAtNextMidnight) / DAY_IN_MILLIS)).toInt()
    }

}

