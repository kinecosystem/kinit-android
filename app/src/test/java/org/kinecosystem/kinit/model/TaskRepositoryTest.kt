package org.kinecosystem.kinit.model

import org.kinecosystem.kinit.mock.MockComponentsProvider
import org.kinecosystem.kinit.model.earn.ChosenAnswers
import org.kinecosystem.kinit.model.earn.isValid
import org.kinecosystem.kinit.repository.TasksRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
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

    private val componentsProvider = MockComponentsProvider()
    private var tasksRepo = TasksRepository(componentsProvider, DEFAULT_TASK)

    @Before
    fun setup() {
        componentsProvider.tasksRepository = tasksRepo
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun testInitialTaskIsValid() {

        val task = tasksRepo.task
        assertNotNull(task)
        assertEquals(task?.isValid(), true)
        System.out.println("initial task $task")
    }

    @Test
    fun testSaveAnswer() {
        val chosen = setChosenAnswers(0, listOf(2))
        val chosenAnswers = tasksRepo.getChosenAnswers()
        assertEquals(chosen.answersIds[0], chosenAnswers[0].answersIds[0])
        assertEquals(chosen.questionId, chosenAnswers[0].questionId)
    }

    @Test
    fun testDeleteChosenAnswersFromCache() {
        setChosenAnswers(0, listOf(1))
        tasksRepo.clearChosenAnswers()
        assertEquals(tasksRepo.getNumOfAnsweredQuestions(), 0)
    }

    @Test
    fun testTwoAnswersSaved() {
        tasksRepo.clearChosenAnswers()
        setChosenAnswers(0, listOf(2))
        val chosen2 = setChosenAnswers(1, listOf(1))
        val answerFromRepo = tasksRepo.getChosenAnswers()[1]
        assertEquals(tasksRepo.getNumOfAnsweredQuestions(), 2)
        assertEquals(chosen2.answersIds[0], answerFromRepo.answersIds[0])
    }


    @Test
    fun testAnswerSavedCount() {
        tasksRepo.clearChosenAnswers()
        val chosenAnswers = setChosenAnswers(0, listOf(1,2))
        assertEquals(1, tasksRepo.getNumOfAnsweredQuestions())
        assertEquals(chosenAnswers.questionId, tasksRepo.getChosenAnswers()[0].questionId)
        assertEquals(chosenAnswers.answersIds.size, tasksRepo.getChosenAnswers()[0].answersIds.size)
    }

    @Test
    fun testMultiAnswersSaved() {
        tasksRepo.clearChosenAnswers()
        val chosenAnswers = setChosenAnswers(0, listOf(1,2,3))
        val answersInRepo = tasksRepo.getChosenAnswers()
        assertEquals(chosenAnswers.questionId, answersInRepo[0].questionId)
        assertEquals(chosenAnswers.answersIds[0], answersInRepo[0].answersIds[0])
        assertEquals(chosenAnswers.answersIds[1], answersInRepo[0].answersIds[1])
        assertEquals(chosenAnswers.answersIds[2], answersInRepo[0].answersIds[2])
    }

    @Test
    fun testIsTaskCompleteFalseAtTheBeginning() {
        tasksRepo.clearChosenAnswers()
        assertEquals(tasksRepo.isTaskComplete(), false)
    }

    @Test
    fun testIsTaskCompleteTrueAtTheEnd() {
        tasksRepo.clearChosenAnswers()

        for (i in tasksRepo.task?.questions?.indices!!) {
            setChosenAnswers(i, listOf(0))
        }
        assertEquals(tasksRepo.isTaskComplete(), true)
    }

    private fun setChosenAnswers(questionIndex: Int, answersIndex: List<Int>): ChosenAnswers {
        val question = tasksRepo.task?.questions?.get(questionIndex)
        val answers = answersIndex.map { index -> question?.answers?.get(index)?.id!! }
        if (question != null && answersIndex.isNotEmpty()) {
            tasksRepo.setChosenAnswers(question.id!!, answers)
        }
        return ChosenAnswers(question?.id!!, answers)
    }
}

