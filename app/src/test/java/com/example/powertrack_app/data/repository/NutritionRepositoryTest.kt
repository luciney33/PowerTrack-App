package com.example.powertrack_app.data.repository

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.entity.PlanNutricionalEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class NutritionRepositoryTest {

    private lateinit var apiService: GymApiService
    private lateinit var repository: NutritionRepository

    private val planEntity = PlanNutricionalEntity(
        id = 1L,
        nombre = "Plan proteico",
        descripcion = "Alto en proteína",
        tipo = 1,
        caloriasObjetivo = 2000,
        proteinasObjetivo = 150.0,
        carbohidratosObjetivo = 200.0,
        grasasObjetivo = 60.0,
        comidas = emptyList()
    )

    @Before
    fun setUp() {
        apiService = mockk()
        repository = NutritionRepository(apiService)
    }

    @Test
    fun `getPlanById exitoso devuelve Success`() = runTest {
        val response = mockk<Response<PlanNutricionalEntity>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns planEntity
        coEvery { apiService.getPlanById(1L) } returns response

        val result = repository.getPlanById(1L)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Plan proteico", (result as NetworkResult.Success).data.nombre)
    }

    @Test
    fun `getPlanById fallido devuelve Error`() = runTest {
        val response = mockk<Response<PlanNutricionalEntity>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        coEvery { apiService.getPlanById(99L) } returns response

        val result = repository.getPlanById(99L)

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_PLAN_NO_ENCONTRADO, (result as NetworkResult.Error).message)
    }

    @Test
    fun `getPlanById con excepcion devuelve Error de red`() = runTest {
        coEvery { apiService.getPlanById(any()) } throws Exception("timeout")

        val result = repository.getPlanById(1L)

        assertTrue(result is NetworkResult.Error)
        assertTrue((result as NetworkResult.Error).message.contains(Constantes.ERROR_DE_RED))
    }

    @Test
    fun `getPlanRecomendado exitoso devuelve Success`() = runTest {
        val response = mockk<Response<PlanNutricionalEntity>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns planEntity
        coEvery { apiService.getPlanRecomendado() } returns response

        val result = repository.getPlanRecomendado()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1L, (result as NetworkResult.Success).data.id)
    }

    @Test
    fun `getPlanRecomendado fallido devuelve Error`() = runTest {
        val response = mockk<Response<PlanNutricionalEntity>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        coEvery { apiService.getPlanRecomendado() } returns response

        val result = repository.getPlanRecomendado()

        assertTrue(result is NetworkResult.Error)
        assertEquals(Constantes.ERROR_PLAN_NUTRICIONAL, (result as NetworkResult.Error).message)
    }

    @Test
    fun `getPlanRecomendado con excepcion devuelve Error de red`() = runTest {
        coEvery { apiService.getPlanRecomendado() } throws Exception("sin conexion")

        val result = repository.getPlanRecomendado()

        assertTrue(result is NetworkResult.Error)
        assertTrue((result as NetworkResult.Error).message.contains(Constantes.ERROR_DE_RED))
    }
}
