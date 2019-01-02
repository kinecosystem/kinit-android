package org.kinecosystem.tippic.viewmodel

import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.HOUR_IN_MILLIS
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.kinecosystem.tippic.daggerCore.TestUtils
import org.kinecosystem.tippic.mocks.MockCategoriesViewModel
import org.kinecosystem.tippic.mocks.MockScheduler
import org.kinecosystem.tippic.navigation.Navigator
import org.kinecosystem.tippic.repository.TasksRepo
import org.kinecosystem.tippic.util.Scheduler
import org.kinecosystem.tippic.viewmodel.earn.CategoriesViewModel
import org.kinecosystem.tippic.viewmodel.earn.CategoryTaskViewModel
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import javax.inject.Inject

private const val SAMPLE_QUIZ_TASK_REWARD = 25
class CategoryTaskViewModelTest {

    @Inject
    lateinit var scheduler: Scheduler

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var model: CategoryTaskViewModel
    private lateinit var taskRepo: TasksRepo

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestUtils.setupCoreComponent().inject(this)
        categoriesViewModel = MockCategoriesViewModel()
        setupRepo()
    }


    private fun setupRepo() {
        val category = categoriesViewModel.categoriesRepository.getCategory("0")
        categoriesViewModel.categoriesRepository.onCategorySelected(category)
        categoriesViewModel.categoriesRepository.onTaskStarted()
        taskRepo = categoriesViewModel.categoriesRepository.currentTaskRepo!!
        model = CategoryTaskViewModel(Mockito.mock(Navigator::class.java), category!!)
    }

    private fun setupTaskWithTime(timeInMillis: Long) {
        val timeInSecs = timeInMillis / 1000
        taskRepo.updateTestTask(timeInSecs)
        model.refreshTaskData()
    }

    @Test
    fun quizRewardCalculation() {
        val time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore)
        val kinReward = model.kinReward.get()?.toInt()
        assertTrue(kinReward == SAMPLE_QUIZ_TASK_REWARD)
    }

    @Test
    fun taskAvailableWhenTaskTimeBeforeOneHour() {
        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore)
        assertTrue(model.shouldShowTask.get())
    }

    @Test
    fun taskNotAvailableWhenTaskTimeInOneHour() {
        val timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        assertFalse(model.shouldShowTask.get())
    }

    @Test
    fun taskDoesNotBecomeAvailableInADayWhenTaskIn2Days() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        var timeIn2days = timeInAday + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        (scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertFalse(model.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableInADay() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        setupTaskWithTime(timeInAday)
        assertFalse(model.shouldShowTask.get())
        (scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertTrue(model.shouldShowTask.get())
    }


    @Test
    fun taskBecomesAvailableInAnHour() {
        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        assertFalse(model.shouldShowTask.get())
        (scheduler as MockScheduler).setCurrentTime(timeIn1hour+10)
        assertTrue(model.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableIn2Days() {
        val currentTime = System.currentTimeMillis()
        val mockScheduler = scheduler as MockScheduler
        mockScheduler.setCurrentTime(currentTime)
        val timeIn1day = currentTime + DAY_IN_MILLIS
        var timeIn2days = timeIn1day + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        System.out.println("Task available in " + model.nextAvailableDate.get())
        assertFalse(model.isAvailableTomorrow.get())
        mockScheduler.setCurrentTime(timeIn1day)
        assertFalse(model.shouldShowTask.get())
        assertTrue(model.isAvailableTomorrow.get())
        mockScheduler.setCurrentTime(timeIn2days)
        assertTrue(model.shouldShowTask.get())
    }

}
