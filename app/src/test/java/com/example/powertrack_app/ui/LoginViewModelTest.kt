package com.example.powertrack_app.ui

import app.cash.turbine.test
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.local.TokenManager
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.domain.usecase.LoginUseCase
import com.example.powertrack_app.ui.common.UiEvent
import com.example.powertrack_app.ui.screens.login.LoginEvent
import com.example.powertrack_app.ui.screens.login.LoginViewModel
import com.example.powertrack_app.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: LoginViewModel

    private val usuarioConPerfil = UsuarioEntity(
        id = 1L, username = "luciap", email = "lucia@test.com",
        nombre = "Lucia", password = "pass", formularioCompletado = true
    )
    private val usuarioSinPerfil = usuarioConPerfil.copy(formularioCompletado = false)
    private val loginResponseConPerfil = LoginResponse(
        accessToken = "tok", refreshToken = "ref",
        usuario = usuarioConPerfil, message = "ok", requires2FA = false
    )
    private val loginResponseSinPerfil = loginResponseConPerfil.copy(usuario = usuarioSinPerfil)

    @Before
    fun setUp() {
        loginUseCase = mockk()
        tokenManager = mockk(relaxed = true)
        viewModel = LoginViewModel(loginUseCase, tokenManager)
    }

    @Test
    fun `username changed actualiza estado`() {
        viewModel.onEvent(LoginEvent.UsernameChanged("luciap"))
        assertEquals("luciap", viewModel.state.value.username)
    }

    @Test
    fun `password changed actualiza estado`() {
        viewModel.onEvent(LoginEvent.PasswordChanged("pass123"))
        assertEquals("pass123", viewModel.state.value.password)
    }

    @Test
    fun `login exitoso con perfil completo emite LoginSuccess`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns NetworkResult.Success(loginResponseConPerfil)

        viewModel.uiEvent.test {
            viewModel.onEvent(LoginEvent.Login)
            assertEquals(UiEvent.LoginSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login exitoso sin perfil emite LoginSuccessNeedsPerfil`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns NetworkResult.Success(loginResponseSinPerfil)

        viewModel.uiEvent.test {
            viewModel.onEvent(LoginEvent.Login)
            assertEquals(UiEvent.LoginSuccessNeedsPerfil, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login exitoso guarda tokens en TokenManager`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns NetworkResult.Success(loginResponseConPerfil)

        viewModel.onEvent(LoginEvent.Login)

        verify { tokenManager.saveAccessToken("tok") }
        verify { tokenManager.saveRefreshToken("ref") }
    }

    @Test
    fun `login fallido actualiza estado con error`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns NetworkResult.Error("Credenciales incorrectas")

        viewModel.onEvent(LoginEvent.Login)

        assertFalse(viewModel.state.value.isLoading)
        assertEquals("Credenciales incorrectas", viewModel.state.value.error)
    }

    @Test
    fun `cambiar username limpia el error previo`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns NetworkResult.Error("Error")
        viewModel.onEvent(LoginEvent.Login)

        viewModel.onEvent(LoginEvent.UsernameChanged("nuevo"))

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `isLoading es false al inicio`() {
        assertFalse(viewModel.state.value.isLoading)
    }
}
