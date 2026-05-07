package com.example.powertrack_app.ui.screens.perfil

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.powertrack_app.ui.common.UiEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.ui.text.input.KeyboardType
import com.example.powertrack_app.ui.theme.PowerTrackTheme

@Composable
fun PerfilFormScreen(
    onPerfilCompletado: () -> Unit,
    viewModel: PerfilFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> onPerfilCompletado()
                is UiEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cuéntanos sobre ti",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Personalizaremos tu experiencia según tu perfil",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        PreguntaSelector(
            titulo = "¿Con qué género te identificas?",
            opciones = listOf("Hombre", "Mujer", "Prefiero no decirlo"),
            seleccionado = state.genero,
            onSeleccion = { viewModel.onEvent(PerfilFormEvent.GeneroChanged(it)) }
        )

        PreguntaSelector(
            titulo = "¿Cuántos años tienes?",
            opciones = listOf("16-29 años", "30-39 años", "40-49 años", "50+ años"),
            seleccionado = state.edad,
            onSeleccion = { viewModel.onEvent(PerfilFormEvent.EdadChanged(it)) }
        )

        PreguntaSelector(
            titulo = "¿Cuál es tu objetivo principal?",
            opciones = listOf("Ganar músculo", "Perder grasa", "Mantenerme", "Perder peso"),
            seleccionado = state.objetivo,
            onSeleccion = { viewModel.onEvent(PerfilFormEvent.ObjetivoChanged(it)) }
        )

        PreguntaSelector(
            titulo = "¿Cuánto llevas entrenando?",
            opciones = listOf("Soy nuevo/a", "Entre 6 meses y 2 años", "Más de 2 años"),
            seleccionado = state.nivel,
            onSeleccion = { viewModel.onEvent(PerfilFormEvent.NivelChanged(it)) }
        )

        PreguntaSelector(
            titulo = "¿Cuántos días puedes entrenar?",
            opciones = listOf("2 días", "3 días", "4 días", "5 días"),
            seleccionado = when (state.diasEntrenamiento) {
                2 -> 0; 3 -> 1; 4 -> 2; else -> 3
            },
            onSeleccion = {
                val dias = when (it) { 0 -> 2; 1 -> 3; 2 -> 4; else -> 5 }
                viewModel.onEvent(PerfilFormEvent.DiasChanged(dias))
            }
        )

        PreguntaSelector(
            titulo = "¿Tienes alguna lesión?",
            opciones = listOf("No tengo", "Espalda", "Rodilla", "Hombro", "Tobillo"),
            seleccionado = state.lesion,
            onSeleccion = { viewModel.onEvent(PerfilFormEvent.LesionChanged(it)) }
        )

        PreguntaSelector(
            titulo = "¿Qué tipo de entrenamiento prefieres?",
            opciones = listOf("Pesas y fuerza", "Cardio", "Mixto"),
            seleccionado = state.preferencia,
            onSeleccion = { viewModel.onEvent(PerfilFormEvent.PreferenciaChanged(it)) }
        )

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        HorizontalDivider()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "¿Cuánto pesas?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = state.peso,
                onValueChange = { viewModel.onEvent(PerfilFormEvent.PesoChanged(it)) },
                label = { Text("Peso en kg (ej: 70)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            HorizontalDivider()
        }

        Button(
            onClick = { viewModel.onEvent(PerfilFormEvent.Guardar) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Empezar mi plan personalizado")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PreguntaSelector(
    titulo: String,
    opciones: List<String>,
    seleccionado: Int,
    onSeleccion: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        opciones.forEachIndexed { index, opcion ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = seleccionado == index,
                    onClick = { onSeleccion(index) }
                )
                Text(
                    text = opcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilFormScreenPreview() {
    PowerTrackTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cuéntanos sobre ti",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Personalizaremos tu experiencia según tu perfil",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                PreguntaSelector(
                    titulo = "¿Con qué género te identificas?",
                    opciones = listOf("Hombre", "Mujer", "Prefiero no decirlo"),
                    seleccionado = 0,
                    onSeleccion = {}
                )
                PreguntaSelector(
                    titulo = "¿Cuántos años tienes?",
                    opciones = listOf("16-29 años", "30-39 años", "40-49 años", "50+ años"),
                    seleccionado = 1,
                    onSeleccion = {}
                )
                PreguntaSelector(
                    titulo = "¿Cuál es tu objetivo principal?",
                    opciones = listOf("Ganar músculo", "Perder grasa", "Mantenerme", "Perder peso"),
                    seleccionado = 0,
                    onSeleccion = {}
                )
                PreguntaSelector(
                    titulo = "¿Cuánto llevas entrenando?",
                    opciones = listOf("Soy nuevo/a", "Entre 6 meses y 2 años", "Más de 2 años"),
                    seleccionado = 1,
                    onSeleccion = {}
                )
                PreguntaSelector(
                    titulo = "¿Cuántos días puedes entrenar?",
                    opciones = listOf("2 días", "3 días", "4 días", "5 días"),
                    seleccionado = 2,
                    onSeleccion = {}
                )
                PreguntaSelector(
                    titulo = "¿Tienes alguna lesión?",
                    opciones = listOf("No tengo", "Espalda", "Rodilla", "Hombro", "Tobillo"),
                    seleccionado = 0,
                    onSeleccion = {}
                )
                PreguntaSelector(
                    titulo = "¿Qué tipo de entrenamiento prefieres?",
                    opciones = listOf("Pesas y fuerza", "Cardio", "Mixto"),
                    seleccionado = 2,
                    onSeleccion = {}
                )

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Empezar mi plan personalizado")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}