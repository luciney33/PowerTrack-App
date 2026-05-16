package com.example.powertrack_app.ui

import app.cash.turbine.test
import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.model.Usuario
import com.example.powertrack_app.domain.usecase.RegisterUseCase
import com.example.powertrack_app.ui.common.UiEvent
import com.example.powertrack_app.ui.screens.register.RegisterEvent
import com.example.powertrack_app.ui.screens.register.RegisterViewModel
import com.example.powertrack_app.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var viewModel: RegisterViewModel

    private val usuarioDomain = Usuario(
        id = 1L, username = "nuevo", email = "nuevo@test.com",
        nombre = "Nuevo", rol = "USER"
    )

    @Before
    fun setUp() {
        registerUseCase = mockk()
        viewModel = RegisterViewModel(registerUseCase)
    }

    private fun rellenarFormulario(
        username: String = "nuevo",
        email: String = "nuevo@test.com",
        nombre: String = "Nuevo",
        password: String = "password123",
        confirm: String = "password123"
    ) {
        viewModel.onEvent(RegisterEvent.UserChanged(username = username))
        viewModel.onEvent(RegisterEvent.UserChanged(email = email))
        viewModel.onEvent(RegisterEvent.UserChanged(nombre = nombre))
        viewModel.onEvent(RegisterEvent.UserChanged(password = password))
        viewModel.onEvent(RegisterEvent.UserChanged(confirmPassword = confirm))
    }

    @Test
    fun `password no coincide muestra error`() = runTest {
        rellenarFormulario(password = "password123", confirm = "diferente1")

        viewModel.onEvent(RegisterEvent.Register)

        assertEquals(Constantes.ERROR_PASSWORDS_NO_COINCIDEN, viewModel.state.value.error)
    }

    @Test
    fun `password menor de 8 caracteres muestra error`() = runTest {
        rellenarFormulario(password = "corta", confirm = "corta")

        viewModel.onEvent(RegisterEvent.Register)

        assertEquals(Constantes.ERROR_PASSWORD_MINIMA, viewModel.state.value.error)
    }

    @Test
    fun `email invalido muestra error`() = runTest {
        rellenarFormulario(email = "correo_invalido")

        viewModel.onEvent(RegisterEvent.Register)

        assertEquals(Constantes.ERROR_EMAIL_INVALIDO, viewModel.state.value.error)
    }

    @Test
    fun `registro exitoso emite RegisterSuccess`() = runTest {
        coEvery { registerUseCase(any(), any(), any(), any()) } returns NetworkResult.Success(usuarioDomain)
        rellenarFormulario()

        viewModel.uiEvent.test {
            viewModel.onEvent(RegisterEvent.Register)
            assertEquals(UiEvent.RegisterSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `registro fallido muestra error en estado`() = runTest {
        coEvery { registerUseCase(any(), any(), any(), any()) } returns NetworkResult.Error("Usuario ya existe")
        rellenarFormulario()

        viewModel.onEvent(RegisterEvent.Register)

        assertEquals("Usuario ya existe", viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `cambiar campo limpia el error previo`() = runTest {
        coEvery { registerUseCase(any(), any(), any(), any()) } returns NetworkResult.Error("Error")
        rellenarFormulario()
        viewModel.onEvent(RegisterEvent.Register)

        viewModel.onEvent(RegisterEvent.UserChanged(username = "otro"))

        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `estado inicial tiene campos vacios`() {
        val estado = viewModel.state.value
        assertTrue(estado.username.isEmpty())
        assertTrue(estado.email.isEmpty())
        assertNull(estado.error)
        assertFalse(estado.isLoading)
    }
}
