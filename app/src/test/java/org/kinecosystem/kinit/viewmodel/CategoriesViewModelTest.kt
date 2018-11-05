package org.kinecosystem.kinit.viewmodel

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.daggerCore.TestUtils
import org.kinecosystem.kinit.mocks.MockCategoriesViewModel
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.util.Scheduler
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import javax.inject.Inject


class CategoriesViewModelTest {

    @Inject
    lateinit var scheduler: Scheduler

    private lateinit var model: MockCategoriesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestUtils.setupCoreComponent().inject(this)
        model = MockCategoriesViewModel(Mockito.mock(Navigator::class.java))

        //Mockito.`when`(userRepository.restoreHints).thenReturn(listOf("1", "2"))
    }


    @Test
    fun checkInitData() {
        model.categoriesRepository.onCategorySelected(model.categoriesRepository.getCategory("0"))
        assertNotNull(model.categoriesRepository.currentTaskRepo?.task)
        assertNull(model.categoriesRepository.currentTaskRepo?.taskInProgress)
    }

    @Test
    fun checkAnswerSubmitted() {
        model.categoriesRepository.onCategorySelected(model.categoriesRepository.getCategory("0"))
        assertNotNull(model.categoriesRepository.currentTaskRepo?.task)
        assertNull(model.categoriesRepository.currentTaskRepo?.taskInProgress)
        model.categoriesRepository.onTaskStarted()
        assertNotNull(model.categoriesRepository.currentTaskRepo?.taskInProgress)

    }

//
//    @Test
//    fun taskRewardCalculation() {
//        val time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
//        setupTaskWithTime(time1hourBefore, SAMPLE_QUESTIONNAIRE_TASK)
//        val kinReward = earnViewModel.kinReward.get()?.toInt()
//        assertTrue(kinReward == earnViewModel.tasksRepository.task?.kinReward)
//    }
//
//    @Test
//    fun taskNotAvailableWhenTaskTimeInOneHour() {
//        val timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
//        setupTaskWithTime(timeIn1hour)
//        assertFalse(earnViewModel.shouldShowTask.get())
//    }
//
//    @Test
//    fun taskAvailableWhenTaskTimeBeforeOneHour() {
//        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
//        setupTaskWithTime(time1hourBefore)
//        assertTrue(earnViewModel.shouldShowTask.get())
//    }
//
//    @Test
//    fun taskBecomesAvailableInAnHour() {
//        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
//        setupTaskWithTime(timeIn1hour)
//        (scheduler as MockScheduler).setCurrentTime(timeIn1hour)
//        assertTrue(earnViewModel.shouldShowTask.get())
//    }
//
//    @Test
//    fun taskBecomesAvailableInADay() {
//        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
//        setupTaskWithTime(timeInAday)
//        (scheduler as MockScheduler).setCurrentTime(timeInAday)
//        assertTrue(earnViewModel.shouldShowTask.get())
//    }
//
//    @Test
//    fun taskDoesNotBecomeAvailableInADayWhenTaskIn2Days() {
//        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
//        var timeIn2days = timeInAday + DAY_IN_MILLIS
//        setupTaskWithTime(timeIn2days)
//        (scheduler as MockScheduler).setCurrentTime(timeInAday)
//        assertFalse(earnViewModel.shouldShowTask.get())
//    }
//
//    @Test
//    fun taskBecomesAvailableIn2Days() {
//        val currentTime = System.currentTimeMillis()
//        val mockScheduler = scheduler as MockScheduler
//        mockScheduler.setCurrentTime(currentTime)
//        val timeIn1day = currentTime + DAY_IN_MILLIS
//        var timeIn2days = timeIn1day + DAY_IN_MILLIS
//        setupTaskWithTime(timeIn2days)
//        System.out.println("Task available in " + earnViewModel.nextAvailableDate.get())
//        assertFalse(earnViewModel.isAvailableTomorrow.get())
//        mockScheduler.setCurrentTime(timeIn1day)
//        assertFalse(earnViewModel.shouldShowTask.get())
//        assertTrue(earnViewModel.isAvailableTomorrow.get())
//        mockScheduler.setCurrentTime(timeIn2days)
//        assertTrue(earnViewModel.shouldShowTask.get())
//    }

}
