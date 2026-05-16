package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.security.SessionManager
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

    private lateinit var repository: GymRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)
        useCase = LogoutUseCase(repository, sessionManager)
    }

    @Test
    fun `logout llama a repository logout`() {
        useCase()
        verify { repository.logout() }
    }

    @Test
    fun `logout limpia la sesion del SessionManager`() {
        useCase()
        verify { sessionManager.clearSession() }
    }

    @Test
    fun `logout llama a ambos en el mismo invoke`() {
        useCase()
        verify(exactly = 1) { repository.logout() }
        verify(exactly = 1) { sessionManager.clearSession() }
    }
}
