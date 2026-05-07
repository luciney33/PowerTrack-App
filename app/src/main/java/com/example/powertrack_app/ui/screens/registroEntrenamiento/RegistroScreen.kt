package com.example.powertrack_app.ui.screens.registroEntrenamiento

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.powertrack_app.ui.common.UiEvent
import com.example.powertrack_app.ui.theme.PowerTrackTheme

@Composable
fun RegistroScreen(
    fecha: String = "",
    viewModel: RegistroViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var mostrarSelector by remember { mutableStateOf(false) }

    LaunchedEffect(fecha) {
        viewModel.setFecha(fecha)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is UiEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    if (mostrarSelector) {
        AlertDialog(
            onDismissRequest = { mostrarSelector = false },
            title = { Text("Añadir ejercicio") },
            text = {
                LazyColumn {
                    items(state.ejercicios.size) { index ->
                        val ejercicio = state.ejercicios[index]
                        TextButton(
                            onClick = {
                                viewModel.onEvent(RegistroEvent.AyadirEjercicio(ejercicio.id, ejercicio.nombre, ejercicio.tipo))
                                mostrarSelector = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(ejercicio.nombre)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarSelector = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Registrar entrenamiento",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Añade los ejercicios de hoy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
        }

        itemsIndexed(state.detalles) { index, detalle ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = detalle.nombreEjercicio,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { viewModel.onEvent(RegistroEvent.EliminarEjercicio(index)) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = detalle.series,
                            onValueChange = { viewModel.onEvent(RegistroEvent.SeriesChanged(index, it)) },
                            label = { Text("Series") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = detalle.repeticiones,
                            onValueChange = { viewModel.onEvent(RegistroEvent.RepeticionesChanged(index, it)) },
                            label = { Text("Reps") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = detalle.peso,
                            onValueChange = { viewModel.onEvent(RegistroEvent.PesoChanged(index, it)) },
                            label = { Text("Kg") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true
                        )
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = { mostrarSelector = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Añadir ejercicio")
            }
        }

        item {
            OutlinedTextField(
                value = state.observaciones,
                onValueChange = { viewModel.onEvent(RegistroEvent.ObservacionesChanged(it)) },
                label = { Text("Observaciones (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }

        item {
            Button(
                onClick = { viewModel.onEvent(RegistroEvent.Guardar) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !state.isLoading && state.detalles.isNotEmpty()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar entrenamiento")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroScreenPreview() {
    PowerTrackTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Registrar entrenamiento",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Añade los ejercicios de hoy",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Press de banca",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = "4",
                                onValueChange = {},
                                label = { Text("Series") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = "10",
                                onValueChange = {},
                                label = { Text("Reps") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = "60.0",
                                onValueChange = {},
                                label = { Text("Kg") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir ejercicio")
                }
            }

            item {
                OutlinedTextField(
                    value = "Buen entrenamiento hoy",
                    onValueChange = {},
                    label = { Text("Observaciones (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }

            item {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Guardar entrenamiento")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}