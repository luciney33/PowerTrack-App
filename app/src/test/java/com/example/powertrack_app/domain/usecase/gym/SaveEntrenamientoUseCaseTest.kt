package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.entity.EntrenamientoEntity
import com.example.powertrack_app.data.repository.TrainingRepository
import com.example.powertrack_app.domain.model.Entrenamiento
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SaveEntrenamientoUseCaseTest {

    private lateinit var repository: TrainingRepository
    private lateinit var useCase: SaveEntrenamientoUseCase

    private val entidadNueva = EntrenamientoEntity(
        id = 0L, usuarioId = 1L, nombre = "Nuevo", descripcion = "Desc"
    )
    private val entidadExistente = EntrenamientoEntity(
        id = 5L, usuarioId = 1L, nombre = "Existente", descripcion = "Desc"
    )
    private val resultadoDomain = Entrenamiento(
        id = 5L, nombre = "Existente", descripcion = "Desc", ejercicios = emptyList()
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SaveEntrenamientoUseCase(repository)
    }

    @Test
    fun `guardar entrenamiento nuevo devuelve Success`() = runTest {
        coEvery { repository.saveEntrenamiento(entidadNueva) } returns NetworkResult.Success(resultadoDomain)

        val result = useCase(entidadNueva)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `guardar entrenamiento existente devuelve Success`() = runTest {
        coEvery { repository.saveEntrenamiento(entidadExistente) } returns NetworkResult.Success(resultadoDomain)

        val result = useCase(entidadExistente)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Existente", (result as NetworkResult.Success).data.nombre)
    }

    @Test
    fun `repositorio falla devuelve Error`() = runTest {
        coEvery { repository.saveEntrenamiento(any()) } returns NetworkResult.Error("Error al guardar")

        val result = useCase(entidadNueva)

        assertTrue(result is NetworkResult.Error)
    }

    @Test
    fun `use case delega directamente al repositorio`() = runTest {
        coEvery { repository.saveEntrenamiento(entidadNueva) } returns NetworkResult.Success(resultadoDomain)

        useCase(entidadNueva)

        coVerify(exactly = 1) { repository.saveEntrenamiento(entidadNueva) }
    }
}
