package com.example.powertrack_app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.powertrack_app.ui.navigation.Screen
import com.example.powertrack_app.ui.screens.calendario.CalendarioScreen
import com.example.powertrack_app.ui.screens.detallePlan.DetallePlanScreen
import com.example.powertrack_app.ui.screens.detalleRutina.DetalleRutinaScreen
import com.example.powertrack_app.ui.screens.ejercicios.EjerciciosScreen
import com.example.powertrack_app.ui.screens.parati.ParaTiScreen
import com.example.powertrack_app.ui.screens.perfil.usuario.PerfilUsuarioScreen
import com.example.powertrack_app.ui.screens.registroEntrenamiento.RegistroScreen
import com.example.powertrack_app.ui.theme.PowerTrackTheme

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute?.contains("ParaTi") == true ||
                            currentRoute?.contains("Home") == true,
                    onClick = {
                        navController.navigate(Screen.ParaTi) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Para ti") }
                )
                NavigationBarItem(
                    selected = currentRoute?.contains("Calendario") == true,
                    onClick = {
                        navController.navigate(Screen.Calendario) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.CalendarMonth, null) },
                    label = { Text("Calendario") }
                )
                NavigationBarItem(
                    selected = currentRoute?.contains("NuevoRegistro") == true,
                    onClick = {
                        navController.navigate(Screen.NuevoRegistro()) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.AddCircle, null) },
                    label = { Text("Registrar") }
                )
                NavigationBarItem(
                    selected = currentRoute?.contains("Perfil") == true,
                    onClick = {
                        navController.navigate(Screen.PerfilUsuario) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ParaTi,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.ParaTi> {
                ParaTiScreen(
                    onVerDetalleRutina = { rutinaId ->
                        navController.navigate(Screen.DetalleRutina(rutinaId))
                    },
                    onVerDetallePlan = { planId ->
                        navController.navigate(Screen.DetallePlan(planId))
                    }
                )
            }
            composable<Screen.DetalleRutina> {
                DetalleRutinaScreen(onBack = { navController.popBackStack() })
            }
            composable<Screen.DetallePlan> {
                DetallePlanScreen(onBack = { navController.popBackStack() })
            }
            composable<Screen.Calendario> {
                CalendarioScreen(
                    onNuevoRegistro = { fecha ->
                        navController.navigate(Screen.NuevoRegistro(fecha))
                    }
                )
            }

            composable<Screen.NuevoRegistro> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.NuevoRegistro>()
                RegistroScreen(fecha = route.fecha)
            }

            composable<Screen.PerfilUsuario> {
                PerfilUsuarioScreen(onLogout = onLogout)
            }
        }
    }
}

@Composable
fun PlaceholderScreen(texto: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = texto)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PowerTrackTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Para ti") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(Icons.Default.FitnessCenter, null) },
                        label = { Text("Ejercicios") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(Icons.Default.AddCircle, null) },
                        label = { Text("Registrar") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Perfil") }
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Para Ti - Tus rutinas y plan nutricional",
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
        }
    }
}