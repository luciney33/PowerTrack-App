package com.example.powertrack_app.ui

import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.domain.model.Entrenamiento
import com.example.powertrack_app.domain.usecase.gym.DeleteEntrenamientoUseCase
import com.example.powertrack_app.domain.usecase.gym.GetEntrenamientosUseCase
import com.example.powertrack_app.ui.screens.gym.listadoEntrenamiento.ListaViewModel
import com.example.powertrack_app.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getEntrenamientosUseCase: GetEntrenamientosUseCase
    private lateinit var deleteEntrenamientoUseCase: DeleteEntrenamientoUseCase
    private lateinit var viewModel: ListaViewModel

    private val entrenamiento1 = Entrenamiento(1L, "Push day", "Pecho", emptyList())
    private val entrenamiento2 = Entrenamiento(2L, "Pull day", "Espalda", emptyList())

    @Before
    fun setUp() {
        getEntrenamientosUseCase = mockk()
        deleteEntrenamientoUseCase = mockk()
    }

    private fun createViewModel() {
        viewModel = ListaViewModel(getEntrenamientosUseCase, deleteEntrenamientoUseCase)
    }

    @Test
    fun `al iniciar carga los entrenamientos correctamente`() = runTest {
        coEvery { getEntrenamientosUseCase() } returns NetworkResult.Success(listOf(entrenamiento1, entrenamiento2))

        createViewModel()

        assertEquals(2, viewModel.state.value.entrenamientos.size)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `error al cargar muestra mensaje de error`() = runTest {
        coEvery { getEntrenamientosUseCase() } returns NetworkResult.Error("Sin conexión")

        createViewModel()

        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.entrenamientos.isEmpty())
    }

    @Test
    fun `eliminar exitoso recarga la lista`() = runTest {
        coEvery { getEntrenamientosUseCase() } returnsMany listOf(
            NetworkResult.Success(listOf(entrenamiento1, entrenamiento2)),
            NetworkResult.Success(listOf(entrenamiento2))
        )
        coEvery { deleteEntrenamientoUseCase(1L) } returns NetworkResult.Success(Unit)
        createViewModel()

        viewModel.eliminar(1L)

        assertEquals(1, viewModel.state.value.entrenamientos.size)
        assertEquals("Pull day", viewModel.state.value.entrenamientos[0].nombre)
    }

    @Test
    fun `eliminar fallido muestra error`() = runTest {
        coEvery { getEntrenamientosUseCase() } returns NetworkResult.Success(listOf(entrenamiento1))
        coEvery { deleteEntrenamientoUseCase(1L) } returns NetworkResult.Error("No eliminado")
        createViewModel()

        viewModel.eliminar(1L)

        assertEquals(Constantes.ERROR_ELIMINAR_ENTRENAMIENTO, viewModel.state.value.error)
    }

    @Test
    fun `cargar de nuevo refresca la lista`() = runTest {
        coEvery { getEntrenamientosUseCase() } returnsMany listOf(
            NetworkResult.Success(listOf(entrenamiento1)),
            NetworkResult.Success(listOf(entrenamiento1, entrenamiento2))
        )
        createViewModel()

        viewModel.cargar()

        assertEquals(2, viewModel.state.value.entrenamientos.size)
    }
}
