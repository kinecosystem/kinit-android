package org.kinecosystem.tippic.viewmodel

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.kinecosystem.tippic.daggerCore.TestUtils
import org.kinecosystem.tippic.mocks.MockCategoriesViewModel
import org.mockito.MockitoAnnotations


class CategoriesViewModelTest {

    private lateinit var model: MockCategoriesViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestUtils.setupCoreComponent().inject(this)
        model = MockCategoriesViewModel()
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
}
