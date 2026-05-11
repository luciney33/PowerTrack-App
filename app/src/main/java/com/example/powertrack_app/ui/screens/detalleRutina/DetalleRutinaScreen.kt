package com.example.powertrack_app.ui.screens.detalleRutina

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.powertrack_app.domain.model.Ejercicio
import com.example.powertrack_app.domain.model.Rutina
import com.example.powertrack_app.ui.theme.PowerTrackTheme

@Composable
fun DetalleRutinaScreen(
    onBack: () -> Unit,
    viewModel: DetalleRutinaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetalleRutinaContent(
        state = state,
        onBack = onBack,
        onVerGif = { viewModel.verGif(it) },
        onRetry = { viewModel.cargar() },
        onCerrarDialog = { viewModel.cerrarDialog() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetalleRutinaContent(
    state: DetalleRutinaState,
    onBack: () -> Unit,
    onVerGif: (String) -> Unit,
    onRetry: () -> Unit,
    onCerrarDialog: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.rutina?.nombre ?: "Detalle rutina") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            state.error != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.error, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = onRetry) { Text("Reintentar") }
                    }
                }
            }
            else -> {
                val rutina = state.rutina
                if (rutina != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        rutina.nombre,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        rutina.descripcion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                        item {
                            Text(
                                "Ejercicios",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(rutina.ejercicios) { ejercicio ->
                            EjercicioCard(
                                ejercicio = ejercicio,
                                onVerGif = { onVerGif(ejercicio.nombre) }
                            )
                        }
                    }
                }
            }
        }
    }

    state.gifDialog?.let { dialog ->
        GifDialog(dialog = dialog, onDismiss = onCerrarDialog)
    }
}

@Composable
private fun EjercicioCard(ejercicio: Ejercicio, onVerGif: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.FitnessCenter,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    ejercicio.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    ejercicio.tipo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (ejercicio.series != null || ejercicio.repeticiones != null || ejercicio.descansoSeg != null) {
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ejercicio.series?.let { InfoChip("$it series") }
                        ejercicio.repeticiones?.let { InfoChip("$it reps") }
                        ejercicio.descansoSeg?.let { InfoChip("${it}s descanso") }
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onVerGif) {
                Text("Ver GIF", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun GifDialog(dialog: GifDialogState, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
                else add(GifDecoder.Factory())
            }
            .build()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialog.ejercicioNombre) },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    dialog.isLoading -> CircularProgressIndicator()
                    dialog.error != null -> Text(
                        dialog.error,
                        color = MaterialTheme.colorScheme.error
                    )
                    dialog.gifUrl != null -> AsyncImage(
                        model = dialog.gifUrl,
                        imageLoader = imageLoader,
                        contentDescription = dialog.ejercicioNombre,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────

private val previewRutina = Rutina(
    id = 1L,
    nombre = "Hipertrofia 3 días",
    descripcion = "Rutina de hipertrofia enfocada en volumen para 3 días semanales",
    tipo = 2,
    ejercicios = listOf(
        Ejercicio(id = 1L, nombre = "Press de banca", tipo = "Pecho", imageUrl = "", descripcion = "", series = 4, repeticiones = 10, descansoSeg = 90),
        Ejercicio(id = 2L, nombre = "Sentadilla", tipo = "Pierna", imageUrl = "", descripcion = "", series = 4, repeticiones = 8, descansoSeg = 120),
        Ejercicio(id = 3L, nombre = "Peso muerto", tipo = "Espalda", imageUrl = "", descripcion = "", series = 3, repeticiones = 6, descansoSeg = 180),
        Ejercicio(id = 4L, nombre = "Dominadas", tipo = "Espalda / Bíceps", imageUrl = "", descripcion = "", series = 3, repeticiones = null, descansoSeg = 90)
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleRutinaScreenPreview() {
    PowerTrackTheme {
        DetalleRutinaContent(
            state = DetalleRutinaState(rutina = previewRutina),
            onBack = {},
            onVerGif = {},
            onRetry = {},
            onCerrarDialog = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleRutinaLoadingPreview() {
    PowerTrackTheme {
        DetalleRutinaContent(
            state = DetalleRutinaState(isLoading = true),
            onBack = {},
            onVerGif = {},
            onRetry = {},
            onCerrarDialog = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleRutinaErrorPreview() {
    PowerTrackTheme {
        DetalleRutinaContent(
            state = DetalleRutinaState(error = "No se pudo cargar la rutina"),
            onBack = {},
            onVerGif = {},
            onRetry = {},
            onCerrarDialog = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GifDialogLoadingPreview() {
    PowerTrackTheme {
        GifDialog(
            dialog = GifDialogState(ejercicioNombre = "Press de banca", isLoading = true),
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GifDialogErrorPreview() {
    PowerTrackTheme {
        GifDialog(
            dialog = GifDialogState(ejercicioNombre = "Sentadilla", error = "GIF no encontrado"),
            onDismiss = {}
        )
    }
}