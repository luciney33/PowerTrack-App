package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.local.TokenManager
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.EjercicioEntity
import com.example.powertrack_app.data.remote.entity.EntrenamientoEntity
import com.example.powertrack_app.data.remote.entity.LoginRequest
import com.example.powertrack_app.data.remote.entity.LoginResponse
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoRequestEntity
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoResponseEntity
import com.example.powertrack_app.data.remote.entity.UsuarioEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GymRepositoryTest {

    private lateinit var apiService: GymApiService
    private lateinit var tokenManager: TokenManager
    private lateinit var repository: GymRepository

    private val usuarioEntity = UsuarioEntity(
        id = 1L, username = "user", email = "user@test.com",
        nombre = "User", password = "pass123", formularioCompletado = true
    )
    private val loginResponse = LoginResponse(
        accessToken = "access123", refreshToken = "refresh123",
        usuario = usuarioEntity, message = "ok", requires2FA = false
    )
    private val entrenamientoEntity = EntrenamientoEntity(
        id = 1L, usuarioId = 1L, nombre = "Rutina A",
        descripcion = "Desc", ejercicios = emptyList()
    )

    @Before
    fun setUp() {
        apiService = mockk()
        tokenManager = mockk(relaxed = true)
        repository = GymRepository(apiService, tokenManager)
    }

    // --- LOGIN ---

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

    // --- REGISTER ---

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

    // --- LOGOUT ---

    @Test
    fun `logout llama a clearTokens`() {
        repository.logout()
        verify { tokenManager.clearTokens() }
    }

    // --- ENTRENAMIENTOS ---

    @Test
    fun `getEntrenamientos exitoso devuelve lista`() = runTest {
        val response = mockk<Response<List<EntrenamientoEntity>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(entrenamientoEntity)
        coEvery { apiService.getEntrenamientos() } returns response

        val result = repository.getEntrenamientos()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Rutina A", result.data[0].nombre)
    }

    @Test
    fun `getEntrenamientos respuesta vacia devuelve lista vacia`() = runTest {
        val response = mockk<Response<List<EntrenamientoEntity>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns null
        coEvery { apiService.getEntrenamientos() } returns response

        val result = repository.getEntrenamientos()

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }

    @Test
    fun `getEntrenamientos fallo devuelve Error`() = runTest {
        val response = mockk<Response<List<EntrenamientoEntity>>>()
        every { response.isSuccessful } returns false
        coEvery { apiService.getEntrenamientos() } returns response

        val result = repository.getEntrenamientos()

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_NO_CARGAR_ENTRENAMIENTOS, (result as NetworkResult.Error).message)
    }

    // --- SAVE ENTRENAMIENTO ---

    @Test
    fun `saveEntrenamiento con id 0 llama a createEntrenamiento`() = runTest {
        val nuevo = entrenamientoEntity.copy(id = 0L)
        val response = mockk<Response<EntrenamientoEntity>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns entrenamientoEntity
        coEvery { apiService.createEntrenamiento(any()) } returns response

        val result = repository.saveEntrenamiento(nuevo)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `saveEntrenamiento con id existente llama a updateEntrenamiento`() = runTest {
        val response = mockk<Response<EntrenamientoEntity>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns entrenamientoEntity
        coEvery { apiService.updateEntrenamiento(any(), any()) } returns response

        val result = repository.saveEntrenamiento(entrenamientoEntity)

        assertTrue(result is NetworkResult.Success)
    }

    // --- DELETE ENTRENAMIENTO ---

    @Test
    fun `deleteEntrenamiento exitoso devuelve Success`() = runTest {
        val response = mockk<Response<Unit>>()
        every { response.isSuccessful } returns true
        coEvery { apiService.deleteEntrenamiento(1L) } returns response

        val result = repository.deleteEntrenamiento(1L)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `deleteEntrenamiento fallido devuelve Error`() = runTest {
        val response = mockk<Response<Unit>>()
        every { response.isSuccessful } returns false
        coEvery { apiService.deleteEntrenamiento(1L) } returns response

        val result = repository.deleteEntrenamiento(1L)

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_NO_ELIMINAR, (result as NetworkResult.Error).message)
    }

    // --- EJERCICIOS ---

    @Test
    fun `getEjercicios exitoso devuelve lista de ejercicios`() = runTest {
        val ejercicioEntity = EjercicioEntity(
            id = 1L, nombre = "Press banca", tipoEntrenamiento = "FUERZA",
            imagenUrl = "", descripcion = "Pecho"
        )
        val response = mockk<Response<List<EjercicioEntity>>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns listOf(ejercicioEntity)
        coEvery { apiService.getEjercicios() } returns response

        val result = repository.getEjercicios()

        assertTrue(result is NetworkResult.Success)
        assertEquals("Press banca", (result as NetworkResult.Success).data[0].nombre)
    }

    @Test
    fun `getEjercicios fallo devuelve Error`() = runTest {
        val response = mockk<Response<List<EjercicioEntity>>>()
        every { response.isSuccessful } returns false
        coEvery { apiService.getEjercicios() } returns response

        val result = repository.getEjercicios()

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_CARGAR_EJERCICIOS, (result as NetworkResult.Error).message)
    }

    // --- REGISTRO ---

    @Test
    fun `createRegistro exitoso devuelve Success`() = runTest {
        val request = RegistroEntrenamientoRequestEntity(
            rutinaId = 1L, fecha = "2025-01-01",
            observaciones = "", detalles = emptyList()
        )
        val responseEntity = RegistroEntrenamientoResponseEntity(
            id = 1L, rutinaId = 1L, fecha = "2025-01-01",
            observaciones = "", detalles = emptyList()
        )
        val response = mockk<Response<RegistroEntrenamientoResponseEntity>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns responseEntity
        coEvery { apiService.createRegistro(any()) } returns response

        val result = repository.createRegistro(request)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `createRegistro fallo devuelve Error`() = runTest {
        val request = RegistroEntrenamientoRequestEntity(
            rutinaId = 1L, fecha = "2025-01-01",
            observaciones = "", detalles = emptyList()
        )
        val response = mockk<Response<RegistroEntrenamientoResponseEntity>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 500
        coEvery { apiService.createRegistro(any()) } returns response

        val result = repository.createRegistro(request)

        assertTrue(result is NetworkResult.Error)
    }
}
