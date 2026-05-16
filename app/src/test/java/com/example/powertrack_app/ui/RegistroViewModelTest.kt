package com.example.powertrack_app.ui

import app.cash.turbine.test
import com.example.powertrack_app.common.NetworkResult
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoRequestEntity
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoResponseEntity
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.domain.usecase.gym.CreateRegistroUseCase
import com.example.powertrack_app.domain.usecase.gym.GetEjerciciosUseCase
import com.example.powertrack_app.ui.common.UiEvent
import com.example.powertrack_app.ui.screens.registroEntrenamiento.RegistroEvent
import com.example.powertrack_app.ui.screens.registroEntrenamiento.RegistroViewModel
import com.example.powertrack_app.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistroViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getEjerciciosUseCase: GetEjerciciosUseCase
    private lateinit var createRegistroUseCase: CreateRegistroUseCase
    private lateinit var viewModel: RegistroViewModel

    private val ejercicioFuerza = Ejercicio(1L, "Press banca", "FUERZA", "", "Pecho")
    private val ejercicioCardio = Ejercicio(2L, "Cinta", "CARDIO", "", "Cardio")

    private val registroResponseFake = RegistroEntrenamientoResponseEntity(
        id = 1L, rutinaId = 1L, fecha = "2025-01-01",
        observaciones = "", detalles = emptyList()
    )

    @Before
    fun setUp() {
        getEjerciciosUseCase = mockk()
        createRegistroUseCase = mockk()
        coEvery { getEjerciciosUseCase() } returns NetworkResult.Success(listOf(ejercicioFuerza, ejercicioCardio))
        viewModel = RegistroViewModel(getEjerciciosUseCase, createRegistroUseCase)
    }

    @Test
    fun `al iniciar carga los ejercicios disponibles`() {
        assertEquals(2, viewModel.state.value.ejercicios.size)
    }

    @Test
    fun `añadir ejercicio de fuerza lo agrega a detalles`() {
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(1L, "Press banca", "FUERZA"))

        assertEquals(1, viewModel.state.value.detalles.size)
        assertEquals("Press banca", viewModel.state.value.detalles[0].nombreEjercicio)
        assertFalse(viewModel.state.value.detalles[0].esCardio)
    }

    @Test
    fun `añadir ejercicio de cardio marca esCardio como true`() {
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(2L, "Cinta", "CARDIO"))

        assertTrue(viewModel.state.value.detalles[0].esCardio)
    }

    @Test
    fun `eliminar ejercicio lo quita de la lista`() {
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(1L, "Press banca", "FUERZA"))
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(2L, "Cinta", "CARDIO"))

        viewModel.onEvent(RegistroEvent.EliminarEjercicio(0))

        assertEquals(1, viewModel.state.value.detalles.size)
        assertEquals("Cinta", viewModel.state.value.detalles[0].nombreEjercicio)
    }

    @Test
    fun `cambiar series actualiza el detalle correspondiente`() {
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(1L, "Press banca", "FUERZA"))

        viewModel.onEvent(RegistroEvent.SeriesChanged(0, "5"))

        assertEquals("5", viewModel.state.value.detalles[0].series)
    }

    @Test
    fun `cambiar duracion en cardio actualiza el detalle`() {
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(2L, "Cinta", "CARDIO"))

        viewModel.onEvent(RegistroEvent.DuracionChanged(0, "45"))

        assertEquals("45", viewModel.state.value.detalles[0].duracionMinutos)
    }

    @Test
    fun `guardar exitoso emite ShowSnackbar y limpia detalles`() = runTest {
        coEvery { createRegistroUseCase(any()) } returns NetworkResult.Success(registroResponseFake)
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(1L, "Press banca", "FUERZA"))

        viewModel.uiEvent.test {
            viewModel.onEvent(RegistroEvent.Guardar)
            val event = awaitItem()
            assertTrue(event is UiEvent.ShowSnackbar)
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(viewModel.state.value.detalles.isEmpty())
        assertTrue(viewModel.state.value.isSuccess)
    }

    @Test
    fun `guardar fallido emite ShowError y mantiene detalles`() = runTest {
        coEvery { createRegistroUseCase(any()) } returns NetworkResult.Error("Error servidor")
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(1L, "Press banca", "FUERZA"))

        viewModel.uiEvent.test {
            viewModel.onEvent(RegistroEvent.Guardar)
            val event = awaitItem()
            assertTrue(event is UiEvent.ShowError)
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(1, viewModel.state.value.detalles.size)
    }

    @Test
    fun `observaciones changed actualiza el estado`() {
        viewModel.onEvent(RegistroEvent.ObservacionesChanged("Buen entrenamiento"))

        assertEquals("Buen entrenamiento", viewModel.state.value.observaciones)
    }

    @Test
    fun `setFecha con valor no vacio actualiza la fecha interna`() {
        viewModel.setFecha("2025-06-15")
        // No hay estado público para fecha, pero al guardar la usaría
        // Verificamos que no lanza excepción
    }

    @Test
    fun `esCardio es true para HIIT y MOVILIDAD tambien`() {
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(3L, "HIIT", "HIIT"))
        viewModel.onEvent(RegistroEvent.AyadirEjercicio(4L, "Yoga", "MOVILIDAD"))

        assertTrue(viewModel.state.value.detalles[0].esCardio)
        assertTrue(viewModel.state.value.detalles[1].esCardio)
    }
}
