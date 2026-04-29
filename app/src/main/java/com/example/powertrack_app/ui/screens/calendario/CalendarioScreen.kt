package com.example.powertrack_app.ui.screens.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.powertrack_app.data.remote.entity.RegistroEntrenamientoResponseEntity
import com.example.powertrack_app.ui.theme.PowerTrackTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarioScreen(
    viewModel: CalendarioViewModel = hiltViewModel()
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
                    Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.cargarRegistros() }) { Text("Reintentar") }
                }
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Mi calendario",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${state.diasEntrenados.size} días entrenados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    CalendarioMensual(
                        diasEntrenados = state.diasEntrenados,
                        onDiaSeleccionado = { viewModel.seleccionarDia(it) }
                    )
                }

                state.registroSeleccionado?.let { registro ->
                    item {
                        Text(
                            text = "Entrenamiento del ${registro.fecha}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(registro.detalles) { detalle ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = detalle.ejercicio.nombre,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = detalle.ejercicio.tipoEntrenamiento,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${detalle.series}x${detalle.repeticiones}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (detalle.peso > 0) {
                                        Text(
                                            text = "${detalle.peso} kg",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (registro.observaciones.isNotBlank()) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "📝 ${registro.observaciones}",
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (state.registros.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("💪", style = MaterialTheme.typography.displayMedium)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Aún no tienes entrenamientos registrados",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarioMensual(
    diasEntrenados: Set<LocalDate>,
    onDiaSeleccionado: (LocalDate) -> Unit
) {
    val hoy = LocalDate.now()
    val mesActual = YearMonth.now()
    val primerDia = mesActual.atDay(1)
    val diasEnMes = mesActual.lengthOfMonth()
    val diasSemana = listOf("L", "M", "X", "J", "V", "S", "D")

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mesActual.month.getDisplayName(TextStyle.FULL, Locale("es"))
                    .replaceFirstChar { it.uppercase() } + " ${mesActual.year}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                diasSemana.forEach { dia ->
                    Text(
                        text = dia,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            val offsetInicio = (primerDia.dayOfWeek.value - 1)
            val totalCeldas = offsetInicio + diasEnMes
            val filas = (totalCeldas + 6) / 7

            for (fila in 0 until filas) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val numeroDia = fila * 7 + col - offsetInicio + 1
                        if (numeroDia < 1 || numeroDia > diasEnMes) {
                            Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                        } else {
                            val fecha = mesActual.atDay(numeroDia)
                            val entrenado = fecha in diasEntrenados
                            val esHoy = fecha == hoy

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            entrenado -> MaterialTheme.colorScheme.primary
                                            esHoy -> MaterialTheme.colorScheme.primaryContainer
                                            else -> androidx.compose.ui.graphics.Color.Transparent
                                        }
                                    )
                                    .clickable { onDiaSeleccionado(fecha) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = numeroDia.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = when {
                                        entrenado -> MaterialTheme.colorScheme.onPrimary
                                        esHoy -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    },
                                    fontWeight = if (esHoy || entrenado) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarioScreenPreview() {
    PowerTrackTheme {
        val diasEjemplo = setOf(
            LocalDate.now().minusDays(1),
            LocalDate.now().minusDays(3),
            LocalDate.now().minusDays(5),
            LocalDate.now()
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Mi calendario",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${diasEjemplo.size} días entrenados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
            }
            item {
                CalendarioMensual(
                    diasEntrenados = diasEjemplo,
                    onDiaSeleccionado = {}
                )
            }
        }
    }
}