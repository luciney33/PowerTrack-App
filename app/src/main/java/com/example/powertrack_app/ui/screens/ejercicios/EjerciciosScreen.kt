package com.example.powertrack_app.ui.screens.ejercicios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.ui.theme.PowerTrackTheme

@Composable
fun EjerciciosScreen(
    viewModel: EjerciciosViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.cargarEjercicios() }) {
                        Text("Reintentar")
                    }
                }
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Ejercicios",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${state.ejercicios.size} ejercicios disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                }
                items(state.ejercicios) { ejercicio ->
                    EjercicioCard(ejercicio)
                }
            }
        }
    }
}

@Composable
private fun EjercicioCard(ejercicio: Ejercicio) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ejercicio.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = ejercicio.tipo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (ejercicio.descripcion.isNotBlank()) {
                    Text(
                        text = ejercicio.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AssistChip(
                onClick = {},
                label = { Text(ejercicio.tipo, style = MaterialTheme.typography.labelSmall) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EjerciciosScreenPreview() {
    PowerTrackTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Ejercicios",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "24 ejercicios disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
            }
            items(listOf(
                Ejercicio(1L, "Press de banca", "PECHO", "", "Ejercicio básico de empuje para pecho"),
                Ejercicio(2L, "Sentadilla", "PIERNAS", "", "Ejercicio compuesto fundamental"),
                Ejercicio(3L, "Dominadas", "ESPALDA", "", "Ejercicio de peso corporal"),
                Ejercicio(4L, "Press militar", "HOMBROS", "", "Ejercicio básico de empuje"),
                Ejercicio(5L, "Curl de bíceps", "BICEPS", "", "Ejercicio básico de aislamiento"),
            )) { ejercicio ->
                EjercicioCard(ejercicio)
            }
        }
    }
}