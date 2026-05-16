package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.data.security.SessionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var repository: GymRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var useCase: LoginUseCase

    private val usuarioEntity = UsuarioEntity(
        id = 1L, username = "luciap", email = "lucia@test.com",
        nombre = "Lucia", password = "pass1234", formularioCompletado = true
    )
    private val loginResponse = LoginResponse(
        accessToken = "token_abc", refreshToken = "refresh_abc",
        usuario = usuarioEntity, message = "ok", requires2FA = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        sessionManager = mockk(relaxed = true)
        useCase = LoginUseCase(repository, sessionManager)
    }

    @Test
    fun `login exitoso devuelve Success y guarda usuario en sesion`() = runTest {
        coEvery { repository.login(any()) } returns NetworkResult.Success(loginResponse)

        val result = useCase("luciap", "pass1234")

        assertTrue(result is NetworkResult.Success)
        assertEquals("token_abc", (result as NetworkResult.Success).data.accessToken)
        verify { sessionManager.setCurrentUser(usuarioEntity) }
    }

    @Test
    fun `login fallido devuelve Error y no guarda sesion`() = runTest {
        coEvery { repository.login(any()) } returns NetworkResult.Error("Credenciales incorrectas")

        val result = useCase("luciap", "wrongpass")

        assertTrue(result is NetworkResult.Error)
        verify(exactly = 0) { sessionManager.setCurrentUser(any()) }
    }

    @Test
    fun `login error de red devuelve Error`() = runTest {
        coEvery { repository.login(any()) } returns NetworkResult.Error("Error de red")

        val result = useCase("luciap", "pass1234")

        assertTrue(result is NetworkResult.Error)
    }
}
