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
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.viewmodel.earn.EarnViewModel
import org.mockito.Mockito
import org.mockito.Mockito.`when`

private const val SAMPLE_QUESTIONNAIRE_TASK = """
{
    "id":"0",
    "title":"A task title",
    "desc":"A few words about the task, what you can find inside",
    "type":"questionnaire",
    "memo":"memo",
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

private const val SAMPLE_QUIZ_TASK = """
{
            "desc": "t",
            "id": "0",
            "items": [
                {
                    "id": "1",
                    "quiz_data": {
                        "answer_id": "4",
                        "explanation": "t" ,
                        "reward": 4
                    },
                    "results": [
                        {
                            "id": "1",
                            "text": "7"
                        },
                        {
                            "id": "2",
                            "text": "21"
                        },
                        {
                            "id": "3",
                            "text": "9"
                        },
                        {
                            "id": "4",
                            "text": "correct"
                        }
                    ],
                    "text": "t" ,
                    "type": "text"
                },
                {
                    "id": "2",
                    "quiz_data": {
                        "answer_id": "2",
                        "explanation": "t" ,
                        "reward": 4
                    },
                    "results": [
                        {
                            "id": "1",
                            "text": "t"
                        },
                        {
                            "id": "2",
                            "text": "t"
                        },
                        {
                            "id ": "3",
                            "text": "t"
                        },
                        {
                            "id": "4",
                            "text": "t"
                        }
                    ],
                    "text": "t" ,
                    "type": "t"
                },
                {
                    "id": "3",
                    "quiz_data": {
                        "answer_id": "4",
                        "explanation": "t" ,
                        "reward": 4
                    },
                    "results": [
                        {
                            "id": "1",
                            "text": "t"
                        },
                        {
                            "id": "2",
                            "text": "t"
                        },
                        {
                            "id": "3 ",
                            "text": "t"
                        },
                        {
                            "id": "4",
                            "text": "t"
                        }
                    ],
                    "text": "t" ,
                    "type": "text"
                },
                {
                    "id": "4",
                    "quiz_data": {
                        "answer_id": "3",
                        "explanation": "t" ,
                        "reward": 4
                    },
                    "results": [
                        {
                            "id": "1",
                            "text": "t"
                        },
                        {
                            "id": "2",
                            "text": "t"
                        },
                        {
                            "id": "3",
                            "text": "t"
                        },
                        {
                            "id": "4",
                            "text": "t"
                        }
                    ],
                    "text": "t" ,
                    "type": "text"
                },
                {
                    " id": "5",
                    "quiz_data": {
                        "answer_id": "1",
                        "explanation": "t" ,
                        "reward": 4
                    },
                    "results": [
                        {
                            "id": "1",
                            "text": "correct"
                        },
                        {
                            "id": "2",
                            "text": "t"
                        },
                        {
                            "id": "3",
                            "text": "t"
                        },
                        {
                            "id": "4",
                            "text": "t"
                        }
                    ],
                    "text":§,
                    "type": "text"
                }
            ],
            "memo": "t",
            "min_client_version_android": "1.0.5",
            "min_client_version_ios": "1.0.0",
            "min_to_complete": 1.0,
            "price": 5,
            "provider": {
                "image_url": "t",
                "name": "t"
            },
            "start_date": 1534671023,
            "tags": [
                "t"
            ],
            "title": "t",
            "type": "quiz",
            "updated_at": 1533818463
        }
"""

private const val SAMPLE_QUIZ_TASK_REWARD = 20

class EarnViewModelTest {

    private val mockComponents = MockComponentsProvider()
    private val mockNavigator: Navigator = Mockito.mock(Navigator::class.java)
    private lateinit var earnViewModelToTest: EarnViewModel

    private fun setupTaskWithTime(timeInMillis: Long, task: String? = null) {
        val timeInSecs = timeInMillis / 1000
        val sampleTask = (task
                ?: SAMPLE_QUESTIONNAIRE_TASK).replace("__START_DATE__", timeInSecs.toString())
        mockComponents.tasksRepository = TasksRepository(mockComponents, sampleTask)
        `when`(mockComponents.wallet.balance).thenReturn(ObservableField("1"))
        earnViewModelToTest = EarnViewModel(mockComponents.tasksRepository, mockComponents.wallet,
                mockComponents.taskService,
                mockComponents.scheduler, mockComponents.analytics,
                mockNavigator)
        earnViewModelToTest.onScreenVisibleToUser()
    }

    @Test
    fun quizRewardCalculation() {
        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore, SAMPLE_QUIZ_TASK)
        val kinReward = earnViewModelToTest.kinReward.get()?.toInt()
        assertTrue(kinReward == SAMPLE_QUIZ_TASK_REWARD)
    }

    @Test
    fun taskRewardCalculation() {
        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore, SAMPLE_QUESTIONNAIRE_TASK)
        val kinReward = earnViewModelToTest.kinReward.get()?.toInt()
        assertTrue(kinReward == earnViewModelToTest.taskRepository.task?.kinReward)
    }

    @Test
    fun taskNotAvailableWhenTaskTimeInOneHour() {
        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        assertFalse(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun taskAvailableWhenTaskTimeBeforeOneHour() {
        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableInAnHour() {
        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        (mockComponents.scheduler as MockScheduler).setCurrentTime(timeIn1hour)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableInADay() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        setupTaskWithTime(timeInAday)
        (mockComponents.scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertTrue(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun taskDoesNotBecomeAvailableInADayWhenTaskIn2Days() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        var timeIn2days = timeInAday + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        (mockComponents.scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertFalse(earnViewModelToTest.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableIn2Days() {
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
