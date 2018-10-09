package org.kinecosystem.kinit.viewmodel

import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.HOUR_IN_MILLIS
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.daggerCore.TestCoreComponent
import org.kinecosystem.kinit.daggerCore.TestCoreComponentProvider
import org.kinecosystem.kinit.mocks.MockScheduler
import org.kinecosystem.kinit.repository.TasksRepository
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.viewmodel.backup.BackupAlertManager
import org.kinecosystem.kinit.viewmodel.earn.EarnViewModel
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

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

private const val SAMPLE_QUIZ_TASK_REWARD = 25
class EarnViewModelTest {


    @Inject
    lateinit var scheduler: Scheduler

    private lateinit var earnViewModel: EarnViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        KinitApplication.coreComponent = TestCoreComponentProvider().coreComponent
        (KinitApplication.coreComponent as TestCoreComponent).inject(this)
    }

    private fun setupTaskWithTime(timeInMillis: Long, task: String? = null) {
        val timeInSecs = timeInMillis / 1000
        val sampleTask = (task
                ?: SAMPLE_QUESTIONNAIRE_TASK).replace("__START_DATE__", timeInSecs.toString())

        earnViewModel = EarnViewModel(mock(BackupAlertManager::class.java))
        earnViewModel.tasksRepository = TasksRepository()
        earnViewModel.tasksRepository.setTask(sampleTask)
        earnViewModel.onScreenVisibleToUser()
    }

    @Test
    fun quizRewardCalculation() {
        val time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore, SAMPLE_QUIZ_TASK)
        val kinReward = earnViewModel.kinReward.get()?.toInt()
        assertTrue(kinReward == SAMPLE_QUIZ_TASK_REWARD)
    }

    @Test
    fun taskRewardCalculation() {
        val time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore, SAMPLE_QUESTIONNAIRE_TASK)
        val kinReward = earnViewModel.kinReward.get()?.toInt()
        assertTrue(kinReward == earnViewModel.tasksRepository.task?.kinReward)
    }

    @Test
    fun taskNotAvailableWhenTaskTimeInOneHour() {
        val timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        assertFalse(earnViewModel.shouldShowTask.get())
    }

    @Test
    fun taskAvailableWhenTaskTimeBeforeOneHour() {
        var time1hourBefore = System.currentTimeMillis() - HOUR_IN_MILLIS
        setupTaskWithTime(time1hourBefore)
        assertTrue(earnViewModel.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableInAnHour() {
        var timeIn1hour = System.currentTimeMillis() + HOUR_IN_MILLIS
        setupTaskWithTime(timeIn1hour)
        (scheduler as MockScheduler).setCurrentTime(timeIn1hour)
        assertTrue(earnViewModel.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableInADay() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        setupTaskWithTime(timeInAday)
        (scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertTrue(earnViewModel.shouldShowTask.get())
    }

    @Test
    fun taskDoesNotBecomeAvailableInADayWhenTaskIn2Days() {
        var timeInAday = System.currentTimeMillis() + DAY_IN_MILLIS
        var timeIn2days = timeInAday + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        (scheduler as MockScheduler).setCurrentTime(timeInAday)
        assertFalse(earnViewModel.shouldShowTask.get())
    }

    @Test
    fun taskBecomesAvailableIn2Days() {
        val currentTime = System.currentTimeMillis()
        val mockScheduler = scheduler as MockScheduler
        mockScheduler.setCurrentTime(currentTime)
        val timeIn1day = currentTime + DAY_IN_MILLIS
        var timeIn2days = timeIn1day + DAY_IN_MILLIS
        setupTaskWithTime(timeIn2days)
        System.out.println("Task available in " + earnViewModel.nextAvailableDate.get())
        assertFalse(earnViewModel.isAvailableTomorrow.get())
        mockScheduler.setCurrentTime(timeIn1day)
        assertFalse(earnViewModel.shouldShowTask.get())
        assertTrue(earnViewModel.isAvailableTomorrow.get())
        mockScheduler.setCurrentTime(timeIn2days)
        assertTrue(earnViewModel.shouldShowTask.get())
    }

}
