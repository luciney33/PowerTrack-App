package com.example.powertrack_app.domain.usecase.gym

import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.repository.GymRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteEntrenamientoUseCaseTest {

    private lateinit var repository: GymRepository
    private lateinit var useCase: DeleteEntrenamientoUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = DeleteEntrenamientoUseCase(repository)
    }

    @Test
    fun `eliminar exitoso devuelve Success`() = runTest {
        coEvery { repository.deleteEntrenamiento(1L) } returns NetworkResult.Success(Unit)

        val result = useCase(1L)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `eliminar fallido devuelve Error`() = runTest {
        coEvery { repository.deleteEntrenamiento(99L) } returns NetworkResult.Error("No se pudo eliminar")

        val result = useCase(99L)

        assertTrue(result is NetworkResult.Error)
        assertEquals("No se pudo eliminar", (result as NetworkResult.Error).message)
    }

    @Test
    fun `llama al repositorio con el id correcto`() = runTest {
        coEvery { repository.deleteEntrenamiento(42L) } returns NetworkResult.Success(Unit)

        useCase(42L)

        coVerify(exactly = 1) { repository.deleteEntrenamiento(42L) }
    }
}
