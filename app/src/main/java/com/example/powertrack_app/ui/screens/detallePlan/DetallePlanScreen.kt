package com.example.powertrack_app.ui.screens.detallePlan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Restaurant
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
import coil.compose.AsyncImage
import com.example.powertrack_app.domain.model.Comida
import com.example.powertrack_app.domain.model.PlanNutricional
import com.example.powertrack_app.ui.theme.PowerTrackTheme

@Composable
fun DetallePlanScreen(
    onBack: () -> Unit,
    viewModel: DetallePlanViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetallePlanContent(
        state = state,
        onBack = onBack,
        onVerFoto = { viewModel.verFoto(it) },
        onRetry = { viewModel.cargar() },
        onCerrarDialog = { viewModel.cerrarDialog() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetallePlanContent(
    state: DetallePlanState,
    onBack: () -> Unit,
    onVerFoto: (String) -> Unit,
    onRetry: () -> Unit,
    onCerrarDialog: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.plan?.nombre ?: "Detalle plan") },
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
                val plan = state.plan
                if (plan != null) {
                    PlanListContent(
                        plan = plan,
                        modifier = Modifier.padding(padding),
                        onVerFoto = onVerFoto
                    )
                }
            }
        }
    }

    state.fotoDialog?.let { dialog ->
        FotoDialog(dialog = dialog, onDismiss = onCerrarDialog)
    }
}

@Composable
private fun PlanListContent(
    plan: PlanNutricional,
    modifier: Modifier = Modifier,
    onVerFoto: (String) -> Unit
) {
    val comidasPorCategoria = plan.comidas.groupBy { it.categoria }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item { MacrosCard(plan) }

        comidasPorCategoria.forEach { (categoria, comidas) ->
            item {
                Text(
                    text = categoria,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(comidas) { comida ->
                ComidaCard(comida = comida, onVerFoto = { onVerFoto(comida.nombre) })
            }
        }
    }
}

@Composable
private fun MacrosCard(plan: PlanNutricional) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        plan.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        plan.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroItem("Calorías", "${plan.caloriasObjetivo}", "kcal")
                MacroItem("Proteínas", "${plan.proteinasObjetivo}g", "")
                MacroItem("Carbos", "${plan.carbohidratosObjetivo}g", "")
                MacroItem("Grasas", "${plan.grasasObjetivo}g", "")
            }
        }
    }
}

@Composable
private fun MacroItem(label: String, valor: String, unidad: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = valor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        if (unidad.isNotEmpty()) {
            Text(
                text = unidad,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ComidaCard(comida: Comida, onVerFoto: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    comida.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${comida.calorias} kcal", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("P: ${comida.proteinas}g", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("C: ${comida.carbohidratos}g", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("G: ${comida.grasas}g", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onVerFoto) {
                Text("Ver foto", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun FotoDialog(dialog: FotoDialogState, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialog.comidaNombre) },
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
                    dialog.fotoUrl != null -> AsyncImage(
                        model = dialog.fotoUrl,
                        contentDescription = dialog.comidaNombre,
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

private val previewPlan = PlanNutricional(
    id = 1L,
    nombre = "Plan de volumen",
    descripcion = "Plan nutricional para ganancia muscular",
    tipo = 1,
    caloriasObjetivo = 2800,
    proteinasObjetivo = 160.0,
    carbohidratosObjetivo = 350.0,
    grasasObjetivo = 80.0,
    comidas = listOf(
        Comida(id = 1L, nombre = "Avena con plátano", calorias = 450, proteinas = 15.0, carbohidratos = 75.0, grasas = 8.0, categoria = "DESAYUNO"),
        Comida(id = 2L, nombre = "Yogur griego con frutos secos", calorias = 320, proteinas = 18.0, carbohidratos = 25.0, grasas = 14.0, categoria = "MERIENDA"),
        Comida(id = 3L, nombre = "Pechuga de pollo con arroz", calorias = 550, proteinas = 45.0, carbohidratos = 60.0, grasas = 10.0, categoria = "ALMUERZO"),
        Comida(id = 4L, nombre = "Salmón con verduras al vapor", calorias = 480, proteinas = 40.0, carbohidratos = 20.0, grasas = 22.0, categoria = "CENA")
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetallePlanScreenPreview() {
    PowerTrackTheme {
        DetallePlanContent(
            state = DetallePlanState(plan = previewPlan),
            onBack = {},
            onVerFoto = {},
            onRetry = {},
            onCerrarDialog = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetallePlanLoadingPreview() {
    PowerTrackTheme {
        DetallePlanContent(
            state = DetallePlanState(isLoading = true),
            onBack = {},
            onVerFoto = {},
            onRetry = {},
            onCerrarDialog = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetallePlanErrorPreview() {
    PowerTrackTheme {
        DetallePlanContent(
            state = DetallePlanState(error = "No se pudo cargar el plan nutricional"),
            onBack = {},
            onVerFoto = {},
            onRetry = {},
            onCerrarDialog = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FotoDialogLoadingPreview() {
    PowerTrackTheme {
        FotoDialog(
            dialog = FotoDialogState(comidaNombre = "Avena con plátano", isLoading = true),
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FotoDialogErrorPreview() {
    PowerTrackTheme {
        FotoDialog(
            dialog = FotoDialogState(comidaNombre = "Salmón con verduras", error = "Imagen no encontrada"),
            onDismiss = {}
        )
    }
}