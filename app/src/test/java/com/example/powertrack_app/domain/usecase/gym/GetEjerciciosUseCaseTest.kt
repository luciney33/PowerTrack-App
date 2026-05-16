package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import com.example.powertrack_app.domain.model.Ejercicio
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetEjerciciosUseCaseTest {

    private lateinit var repository: GymRepository
    private lateinit var useCase: GetEjerciciosUseCase

    private val ejercicioFake = Ejercicio(
        id = 1L, nombre = "Press banca", tipo = "FUERZA",
        imageUrl = "", descripcion = "Ejercicio de pecho"
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetEjerciciosUseCase(repository)
    }

    @Test
    fun `devuelve Success con lista de ejercicios`() = runTest {
        coEvery { repository.getEjercicios() } returns NetworkResult.Success(listOf(ejercicioFake))

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Press banca", result.data[0].nombre)
    }

    @Test
    fun `devuelve Error cuando el repositorio falla`() = runTest {
        coEvery { repository.getEjercicios() } returns NetworkResult.Error("Error al cargar ejercicios")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun `tipo de ejercicio se mapea correctamente`() = runTest {
        val cardio = ejercicioFake.copy(tipo = "CARDIO")
        coEvery { repository.getEjercicios() } returns NetworkResult.Success(listOf(cardio))

        val result = useCase()

        assertEquals("CARDIO", (result as NetworkResult.Success).data[0].tipo)
    }
}
