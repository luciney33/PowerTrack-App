package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.local.TokenManager
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.LoginRequest
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var apiService: GymApiService
    private lateinit var tokenManager: TokenManager
    private lateinit var repository: AuthRepository

    private val usuarioEntity = UsuarioEntity(
        id = 1L, username = "user", email = "user@test.com",
        nombre = "User", password = "pass123", formularioCompletado = true
    )
    private val loginResponse = LoginResponse(
        accessToken = "access123", refreshToken = "refresh123",
        usuario = usuarioEntity, message = "ok", requires2FA = false
    )

    @Before
    fun setUp() {
        apiService = mockk()
        tokenManager = mockk(relaxed = true)
        repository = AuthRepository(apiService, tokenManager)
    }

    @Test
    fun `login exitoso devuelve Success y guarda tokens`() = runTest {
        val response = mockk<Response<LoginResponse>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns loginResponse
        coEvery { apiService.login(any()) } returns response

        val result = repository.login(LoginRequest("user", "pass"))

        assertTrue(result is NetworkResult.Success)
        assertEquals("access123", (result as NetworkResult.Success).data.accessToken)
        verify { tokenManager.saveAccessToken("access123") }
        verify { tokenManager.saveRefreshToken("refresh123") }
    }

    @Test
    fun `login con respuesta fallida devuelve Error`() = runTest {
        val response = mockk<Response<LoginResponse>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        coEvery { apiService.login(any()) } returns response

        val result = repository.login(LoginRequest("user", "wrongpass"))

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_USUARIO_PASSWORD_INCORRECTOS, (result as NetworkResult.Error).message)
    }

    @Test
    fun `login con excepcion devuelve Error de red`() = runTest {
        coEvery { apiService.login(any()) } throws Exception("timeout")

        val result = repository.login(LoginRequest("user", "pass"))

        assertTrue(result is NetworkResult.Error)
        assertTrue((result as NetworkResult.Error).message.contains(Constantes.ERROR_DE_RED))
    }

    @Test
    fun `register exitoso devuelve Success`() = runTest {
        val response = mockk<Response<UsuarioEntity>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns usuarioEntity
        coEvery { apiService.register(any()) } returns response

        val result = repository.register(usuarioEntity)

        assertTrue(result is NetworkResult.Success)
        assertEquals("user", (result as NetworkResult.Success).data.username)
    }

    @Test
    fun `register fallido devuelve Error con codigo`() = runTest {
        val response = mockk<Response<UsuarioEntity>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 409
        coEvery { apiService.register(any()) } returns response

        val result = repository.register(usuarioEntity)

        assertTrue(result is NetworkResult.Error)
        assertTrue((result as NetworkResult.Error).message.contains("409"))
    }

    @Test
    fun `logout llama a clearTokens`() {
        repository.logout()
        verify { tokenManager.clearTokens() }
    }
}
