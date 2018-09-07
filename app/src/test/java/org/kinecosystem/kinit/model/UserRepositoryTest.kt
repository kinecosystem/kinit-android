package org.kinecosystem.kinit.model

import org.junit.Before
import org.junit.Test
import org.kinecosystem.kinit.mock.MockComponentsProvider
import org.kinecosystem.kinit.repository.UserRepository

class UserRepositoryTest {
    private val componentsProvider = MockComponentsProvider()
    private val userRepository = UserRepository(componentsProvider)


    @Before
    fun setup() {
        componentsProvider.userRepository = userRepository
    }

    @Test
    fun testSaveRetrieveHints(){
        val restoreHints: List<String> = listOf("3", "7")
        userRepository.restoreHints = restoreHints
        for (i in userRepository.restoreHints.indices)
            assert(userRepository.restoreHints[i] == restoreHints[i])
    }
}