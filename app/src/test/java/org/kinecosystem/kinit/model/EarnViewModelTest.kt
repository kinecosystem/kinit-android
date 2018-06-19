package org.kinecosystem.kinit.model

import android.databinding.ObservableField
import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.HOUR_IN_MILLIS
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.kinecosystem.kinit.mock.MockComponentsProvider
import org.kinecosystem.kinit.mock.MockScheduler
import org.kinecosystem.kinit.navigation.Navigator
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.kinecosystem.kinit.viewmodel.earn.EarnViewModel
import org.mockito.Mockito
import org.mockito.Mockito.`when`

private const val SAMPLE_QUESTIONNAIRE = """
{
    "id":"0",
    "title":"A questionnaire title",
    "desc":"A few words about the questionnaire, what you can find inside",
    "type":"questionnaire",
    "price":100,
    "start_date":__START_DATE__,
    "min_to_complete":6,
    "tags":["example"],
    "provider":{"name": "Kinit Team","image_url": "an image url"},
    "items":[
        {"id":"435","text":"How did you hear about Kin?","type":"text",
            "results":[{"id":"235","text":"Friend / Family"},{"id":"34636363","text":"Facebook group"},{"id":"35345","text":"Blog"},
            {"id":"747474574","text":"Internet Article"},
            {"id":"34535","text":"Newspaper Article"},
            {"id":"46346","text":"Other"}]},
        {"id":"4633","text":"Do you hold any cryptocurrency?","type":"text",
            "results":[{"id":"7665", "text":"Yes"},{"id":"35535", "text":"No"},{"id":"6546", "text":"What is cryptocurrency?"}]}
    ]
}"""

class EarnViewModelTest {

    private val mockComponents = MockComponentsProvider()
    private val mockNavigator: Navigator = Mockito.mock(Navigator::class.java)
    private lateinit var earnViewModelToTest: EarnViewModel

    private fun setupTaskWithTime(timeInMillis: Long) {
        val timeInSecs = timeInMillis / 1000
        var task = SAMPLE_QUESTIONNAIRE.replace("__START_DATE__", timeInSecs.toString())
        mockComponents.questionnaireRepository = QuestionnaireRepository(mockComponents, task)
        `when`(mockComponents.wallet.balance).thenReturn(ObservableField("1"))
        earnViewModelToTest = EarnViewModel(mockComponents.questionnaireRepository, mockComponents.wallet, mockComponents.taskService,
             mockComponents.scheduler, mockComponents.analytics,
            mockNavigator)
        earnViewModelToTest.onScreenVisibleToUser()
    }

    @Test
    fun questionnaireNotAvailableWhenTaskTimeInOneHour() {
        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        assertFalse(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun questionnaireAvailableWhenTaskTimeBeforeOneHour() {
        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun questionnaireBecomesAvailableInAnHour() {
        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        (mockComponents.scheduler as MockScheduler).setCurrentTime(timeIn1hour)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun questionnaireBecomesAvailableInADay() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        setupTaskWithTime(timeInAday)
        (mockComponents.scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun questionnaireDoesNotBecomeAvailableInADayWhenTaskIn2Days() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        var timeIn2days = timeInAday + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        (mockComponents.scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertFalse(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun questionnaireBecomesAvailableIn2Days() {
        val currentTime = System.currentTimeMillis()
        val mockScheduler = mockComponents.scheduler
        mockScheduler.setCurrentTime(currentTime)
        val timeIn1day = currentTime + DAY_IN_MILLIS
        var timeIn2days = timeIn1day + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        System.out.println("Task available in " + earnViewModelToTest.nextAvailableDate.get())
        assertFalse(earnViewModelToTest.isAvailableTomorrow.get())
        mockScheduler.setCurrentTime(timeIn1day)
        assertFalse(earnViewModelToTest.shouldShowTask.get())
        assertTrue(earnViewModelToTest.isAvailableTomorrow.get())
        mockScheduler.setCurrentTime(timeIn2days)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

}
