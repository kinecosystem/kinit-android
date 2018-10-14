package org.kinecosystem.kinit.viewmodel.earn

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.graphics.Color
import android.util.Log
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.analytics.Analytics
import org.kinecosystem.kinit.analytics.Events
import org.kinecosystem.kinit.model.earn.Category
import org.kinecosystem.kinit.model.earn.Task
import org.kinecosystem.kinit.model.earn.isQuiz
import org.kinecosystem.kinit.model.earn.tagsString
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.CategoriesRepository
import org.kinecosystem.kinit.util.Scheduler
import javax.inject.Inject

class CategoryTaskViewModel(private val category: Category) {

    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    var headerTitle = ObservableField<String>()
    var headerImage = ObservableField<String>()
    var bgColor = ObservableInt(Color.BLUE)
    var tasksCount = ObservableInt()
    var taskCountVisibility = ObservableBoolean(false)
    var shouldShowTask = ObservableBoolean(true)
    var shouldShowTaskNotAvailableYet = ObservableBoolean()
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
    val task: Task?


    private var scheduledRunnable: Runnable? = null

    init {
        KinitApplication.coreComponent.inject(this)
        // isTaskStarted = tasksRepository.isTaskStarted
        task = categoriesRepository.getTask(category)?.task
        Log.d("###", "### got task $task")
        bindCategoryData()
        bindTaskData()
    }

    fun startTask() {
        //tasksRepository.onTaskStarted()
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

    private fun bindTaskData() {
        task?.let {
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
    }

    private fun convertMinToCompleteToString(minToComplete: Float?): String =
            when {
                minToComplete == null -> "0"
                (minToComplete * 10).toInt() % 10 == 0 -> minToComplete.toInt().toString()
                else -> minToComplete.toString()
            }

    private fun bindCategoryData() {
        headerTitle.set(category.title)
        category.uiData?.let {
            headerImage.set(it.headerImageUrl)
            it.color?.let {
                val color = Color.parseColor(it)
                bgColor.set(color)
            }
        }
        updateTaskCount()
    }

    fun onBackPressed() {
        navigator.navigateTo(Navigator.Destination.MAIN_SCREEN)
    }

    private fun updateTaskCount() {
        taskCountVisibility.set(category.availableTasksCount > 1)
        tasksCount.set(category.availableTasksCount)
    }

}

