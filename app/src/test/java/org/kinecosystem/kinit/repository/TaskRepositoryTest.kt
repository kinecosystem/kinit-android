package org.kinecosystem.kinit.repository

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.daggerTestCore.TestCoreComponentProvider
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.isValid
import org.mockito.MockitoAnnotations
import java.io.UnsupportedEncodingException

private const val DEFAULT_TASK = """
{
    "id":"0",
    "title":"What is crypto?",
    "desc":"A few words about the questionnaire, what you can find inside",
    "type":"questionnaire",
    "memo":"memo",
    "price":150,
    "start_date":1519499896671,
    "min_to_complete":6,
    "tags":["kin","crypto","product","research"],
    "provider":{"name": "Kin Team","image_url": "https://s3.amazonaws.com/kinapp-static/brand_img/dunkin.png"},
    "updated_at":1519499896671,
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

class TaskRepositoryTest {

    private lateinit var coreComponentProvider: TestCoreComponentProvider
    private lateinit var tasksRepository: TasksRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        coreComponentProvider = TestCoreComponentProvider()
        KinitApplication.coreComponent = coreComponentProvider.coreComponent
        tasksRepository = TasksRepository(coreComponentProvider.dataStoreProvider, DEFAULT_TASK)
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun testInitialTaskIsValid() {

        val task = tasksRepository.task
        Assert.assertNotNull(task)
        Assert.assertEquals(task?.isValid(), true)
        System.out.println("initial task $task")
    }

    @Test
    fun testSaveAnswer() {
        val chosen = setChosenAnswers(0, listOf(2))
        val chosenAnswers = tasksRepository.getChosenAnswers()
        Assert.assertEquals(chosen.answersIds[0], chosenAnswers[0].answersIds[0])
        Assert.assertEquals(chosen.questionId, chosenAnswers[0].questionId)
    }

    @Test
    fun testDeleteChosenAnswersFromCache() {
        setChosenAnswers(0, listOf(1))
        tasksRepository.clearChosenAnswers()
        Assert.assertEquals(tasksRepository.getNumOfAnsweredQuestions(), 0)
    }

    @Test
    fun testTwoAnswersSaved() {
        tasksRepository.clearChosenAnswers()
        setChosenAnswers(0, listOf(2))
        val chosen2 = setChosenAnswers(1, listOf(1))
        val answerFromRepo = tasksRepository.getChosenAnswers()[1]
        Assert.assertEquals(tasksRepository.getNumOfAnsweredQuestions(), 2)
        Assert.assertEquals(chosen2.answersIds[0], answerFromRepo.answersIds[0])
    }


    @Test
    fun testAnswerSavedCount() {
        tasksRepository.clearChosenAnswers()
        val chosenAnswers = setChosenAnswers(0, listOf(1,2))
        Assert.assertEquals(1, tasksRepository.getNumOfAnsweredQuestions())
        Assert.assertEquals(chosenAnswers.questionId, tasksRepository.getChosenAnswers()[0].questionId)
        Assert.assertEquals(chosenAnswers.answersIds.size, tasksRepository.getChosenAnswers()[0].answersIds.size)
    }

    @Test
    fun testMultiAnswersSaved() {
        tasksRepository.clearChosenAnswers()
        val chosenAnswers = setChosenAnswers(0, listOf(1,2,3))
        val answersInRepo = tasksRepository.getChosenAnswers()
        Assert.assertEquals(chosenAnswers.questionId, answersInRepo[0].questionId)
        Assert.assertEquals(chosenAnswers.answersIds[0], answersInRepo[0].answersIds[0])
        Assert.assertEquals(chosenAnswers.answersIds[1], answersInRepo[0].answersIds[1])
        Assert.assertEquals(chosenAnswers.answersIds[2], answersInRepo[0].answersIds[2])
    }

    @Test
    fun testIsTaskCompleteFalseAtTheBeginning() {
        tasksRepository.clearChosenAnswers()
        Assert.assertEquals(tasksRepository.isTaskComplete(), false)
    }

    @Test
    fun testIsTaskCompleteTrueAtTheEnd() {
        tasksRepository.clearChosenAnswers()

        for (i in tasksRepository.task?.questions?.indices!!) {
            setChosenAnswers(i, listOf(0))
        }
        Assert.assertEquals(tasksRepository.isTaskComplete(), true)
    }

    private fun setChosenAnswers(questionIndex: Int, answersIndex: List<Int>): ChosenAnswers {
        val question = tasksRepository.task?.questions?.get(questionIndex)
        val answers = answersIndex.map { index -> question?.answers?.get(index)?.id!! }
        if (question != null && answersIndex.isNotEmpty()) {
            tasksRepository.setChosenAnswers(question.id!!, answers)
        }
        return ChosenAnswers(question?.id!!, answers)
    }
}

