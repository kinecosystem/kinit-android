package org.kinecosystem.tippic.repository

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.kinecosystem.tippic.daggerCore.TestUtils
import org.kinecosystem.tippic.mocks.MockCategoriesViewModel
import org.kinecosystem.tippic.model.earn.ChosenAnswers
import org.kinecosystem.tippic.model.earn.isValid
import org.kinecosystem.tippic.viewmodel.earn.CategoriesViewModel
import org.mockito.MockitoAnnotations
import java.io.UnsupportedEncodingException

class TaskTest {


    private lateinit var model: CategoriesViewModel
    private lateinit var taskRepo: TasksRepo


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestUtils.setupCoreComponent().inject(this)
        model = MockCategoriesViewModel()
        setupRepo()
    }


    private fun setupRepo() {
        model.categoriesRepository.onCategorySelected(model.categoriesRepository.getCategory("0"))
        model.categoriesRepository.onTaskStarted()
        taskRepo = model.categoriesRepository.currentTaskRepo!!
    }

    @Test
    @Throws(UnsupportedEncodingException::class)
    fun testInitialTaskIsValid() {
        assertNotNull(taskRepo.task)
        assertEquals(taskRepo.task?.isValid(), true)
        System.out.println("initial task $taskRepo.task")
    }

    @Test
    fun testSaveAnswer() {
        val chosen = setChosenAnswers(0, listOf(2))
        val chosenAnswers = taskRepo.getChosenAnswers()
        assertEquals(chosen.answersIds[0], chosenAnswers!![0].answersIds[0])
        assertEquals(chosen.questionId, chosenAnswers[0].questionId)
    }

    private fun setChosenAnswers(questionIndex: Int, answersIndex: List<Int>): ChosenAnswers {
        val question = taskRepo.task?.questions?.get(questionIndex)
        val answers = answersIndex.map { index -> question?.answers?.get(index)?.id!! }
        if (question != null && answersIndex.isNotEmpty()) {
            taskRepo.setChosenAnswers(question.id!!, answers)
        }
        return ChosenAnswers(question?.id!!, answers)
    }

    @Test
    fun testDeleteChosenAnswersFromCache() {
        setChosenAnswers(0, listOf(1))
        taskRepo.clearChosenAnswers()
        Assert.assertEquals(taskRepo.getNumOfAnsweredQuestions(), 0)
    }

    @Test
    fun testTwoAnswersSaved() {
        taskRepo.clearChosenAnswers()
        setChosenAnswers(0, listOf(2))
        val chosen2 = setChosenAnswers(1, listOf(1))
        val answerFromRepo = taskRepo.getChosenAnswers()[1]
        assertEquals(taskRepo.getNumOfAnsweredQuestions(), 2)
        assertEquals(chosen2.answersIds[0], answerFromRepo.answersIds[0])
    }


    @Test
    fun testAnswerSavedCount() {
        taskRepo.clearChosenAnswers()
        val chosenAnswers = setChosenAnswers(0, listOf(1, 2))
        assertEquals(chosenAnswers.questionId, taskRepo.getChosenAnswers()[0].questionId)
        assertEquals(1, taskRepo.getNumOfAnsweredQuestions())
        assertEquals(chosenAnswers.answersIds.size, taskRepo.getChosenAnswers()[0].answersIds.size)
    }

    @Test
    fun testMultiAnswersSaved() {
        taskRepo.clearChosenAnswers()
        val chosenAnswers = setChosenAnswers(0, listOf(1, 2, 3))
        val answersInRepo = taskRepo.getChosenAnswers()
        assertEquals(chosenAnswers.questionId, answersInRepo[0].questionId)
        assertEquals(chosenAnswers.answersIds[0], answersInRepo[0].answersIds[0])
        assertEquals(chosenAnswers.answersIds[1], answersInRepo[0].answersIds[1])
        assertEquals(chosenAnswers.answersIds[2], answersInRepo[0].answersIds[2])
    }

    @Test
    fun testIsTaskCompleteFalseAtTheBeginning() {
        taskRepo.clearChosenAnswers()
        Assert.assertEquals(taskRepo.isTaskComplete(), false)
    }

    @Test
    fun taskRepo() {
        taskRepo.clearChosenAnswers()
        for (i in taskRepo.task?.questions?.indices!!) {
            setChosenAnswers(i, listOf(0))
        }
        Assert.assertEquals(taskRepo.isTaskComplete(), true)
    }

}

