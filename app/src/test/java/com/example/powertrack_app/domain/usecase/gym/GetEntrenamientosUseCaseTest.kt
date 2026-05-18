package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.TrainingRepository
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.domain.model.Entrenamiento
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetEntrenamientosUseCaseTest {

    private lateinit var repository: TrainingRepository
    private lateinit var useCase: GetEntrenamientosUseCase

    private val entrenamientoFake = Entrenamiento(
        id = 1L, nombre = "Push day", descripcion = "Pecho y tríceps", ejercicios = emptyList()
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetEntrenamientosUseCase(repository)
    }

    @Test
    fun `devuelve Success con lista de entrenamientos`() = runTest {
        coEvery { repository.getEntrenamientos() } returns NetworkResult.Success(listOf(entrenamientoFake))

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Push day", result.data[0].nombre)
    }

    @Test
    fun `devuelve Error cuando el repositorio falla`() = runTest {
        coEvery { repository.getEntrenamientos() } returns NetworkResult.Error("Sin conexión")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Sin conexión", (result as NetworkResult.Error).message)
    }

    @Test
    fun `devuelve lista vacia si no hay entrenamientos`() = runTest {
        coEvery { repository.getEntrenamientos() } returns NetworkResult.Success(emptyList())

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }
}
