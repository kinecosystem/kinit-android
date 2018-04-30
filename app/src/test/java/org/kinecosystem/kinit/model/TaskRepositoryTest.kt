package org.kinecosystem.kinit.model

import org.kinecosystem.kinit.mock.MockComponentsProvider
import org.kinecosystem.kinit.model.earn.ChosenAnswer
import org.kinecosystem.kinit.model.earn.isValid
import org.kinecosystem.kinit.repository.QuestionnaireRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.UnsupportedEncodingException

private const val DEFAULT_QUESTIONNAIRE = """
{
    "id":"0",
    "title":"What is crypto?",
    "desc":"A few words about the questionnaire, what you can find inside",
    "type":"questionnaire",
    "price":150,
    "start_date":1519499896671,
    "min_to_complete":6,
    "tags":["kin","crypto","product","research"],
    "provider":{"name": "Kin Team","image_url": "https://s3.amazonaws.com/kinapp-static/brand_img/dunkin.png"},
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
    private var questionnaireRepo = QuestionnaireRepository(componentsProvider, DEFAULT_QUESTIONNAIRE)

    @Before
    fun setup() {
        componentsProvider.questionnaireRepository = questionnaireRepo
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun testInitialQuestionnaireIsValid() {

        val questionnaire = questionnaireRepo.task

        assertNotNull(questionnaire)
        assertEquals(questionnaire?.isValid(), true)
        System.out.println("initial task $questionnaire")
    }

    @Test
    fun testSaveAnswer() {
        val chosen = setChosenAnswer(0, 2)
        val chosenAnswers = questionnaireRepo.getChosenAnswers()
        assertEquals(chosen.answerId, chosenAnswers[0].answerId)
        assertEquals(chosen.questionId, chosenAnswers[0].questionId)
    }

    @Test
    fun testDeleteChosenAnswersFromCache() {
        setChosenAnswer(0, 1)
        questionnaireRepo.clearChosenAnswers()
        assertEquals(questionnaireRepo.getNumOfChosenAnswers(), 0)
    }

    @Test
    fun testTwoAnswersSaved() {
        questionnaireRepo.clearChosenAnswers()
        setChosenAnswer(0, 2)
        val chosen2 = setChosenAnswer(1, 1)
        assertEquals(questionnaireRepo.getNumOfChosenAnswers(), 2)
        assertEquals(chosen2.answerId, questionnaireRepo.getChosenAnswer(chosen2.questionId))
    }

    @Test
    fun testIsQuestionnaireCompleteFalseAtTheBeginning() {
        questionnaireRepo.clearChosenAnswers()
        assertEquals(questionnaireRepo.isQuestionnaireComplete(), false)
    }

    @Test
    fun testIsQuestionnaireCompleteTrueAtTheEnd() {
        questionnaireRepo.clearChosenAnswers()

        for (i in questionnaireRepo.task?.questions?.indices!!) {
            setChosenAnswer(i, 0)
        }
        assertEquals(questionnaireRepo.isQuestionnaireComplete(), true)
    }

    private fun setChosenAnswer(questionIndex: Int, answerIndex: Int): ChosenAnswer {
        val question = questionnaireRepo.task?.questions?.get(questionIndex)
        val answer = question?.answers?.get(answerIndex)
        if (question != null && answer != null) {
            questionnaireRepo.setChosenAnswer(question?.id!!, answer?.id!!)
        }
        return ChosenAnswer(question?.id!!, answer?.id!!)
    }
}

