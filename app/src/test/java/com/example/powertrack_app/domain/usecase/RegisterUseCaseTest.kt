package com.example.powertrack_app.domain.usecase

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.domain.model.Usuario
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {

    private lateinit var repository: GymRepository
    private lateinit var useCase: RegisterUseCase

    private val usuarioDomain = Usuario(
        id = 1L, username = "nuevo", email = "nuevo@test.com",
        nombre = "Nuevo Usuario", rol = "USER"
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = RegisterUseCase(repository)
    }

    @Test
    fun `password menor de 8 caracteres devuelve Error sin llamar al repositorio`() = runTest {
        val result = useCase("user", "user@test.com", "User", "corta")

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_PASSWORD_MINIMA, (result as NetworkResult.Error).message)
        coVerify(exactly = 0) { repository.register(any()) }
    }

    @Test
    fun `password con exactamente 8 caracteres llama al repositorio`() = runTest {
        coEvery { repository.register(any()) } returns NetworkResult.Success(usuarioDomain)

        val result = useCase("user", "user@test.com", "User", "12345678")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { repository.register(any()) }
    }

    @Test
    fun `registro exitoso devuelve Success con el usuario`() = runTest {
        coEvery { repository.register(any()) } returns NetworkResult.Success(usuarioDomain)

        val result = useCase("nuevo", "nuevo@test.com", "Nuevo Usuario", "password123")

        assertTrue(result is NetworkResult.Success)
        assertEquals("nuevo", (result as NetworkResult.Success).data.username)
    }

    @Test
    fun `repositorio devuelve Error lo propaga correctamente`() = runTest {
        coEvery { repository.register(any()) } returns NetworkResult.Error("El usuario ya existe")

        val result = useCase("user", "user@test.com", "User", "password123")

        assertTrue(result is NetworkResult.Error)
        assertEquals("El usuario ya existe", (result as NetworkResult.Error).message)
    }

    @Test
    fun `se envia el username correcto al repositorio`() = runTest {
        coEvery { repository.register(any()) } returns NetworkResult.Success(usuarioDomain)

        useCase("luciatest", "lucia@test.com", "Lucia", "secreta123")

        coVerify {
            repository.register(match { it.username == "luciatest" && it.email == "lucia@test.com" })
        }
    }
}
