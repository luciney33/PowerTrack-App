package com.example.powertrack_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.powertrack_app.ui.screens.HomeScreen
import com.example.powertrack_app.ui.screens.login.LoginScreen
import com.example.powertrack_app.ui.screens.login.LogoutViewModel
import com.example.powertrack_app.ui.screens.perfil.PerfilFormScreen
import com.example.powertrack_app.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation(
    startDestination: Screen = Screen.Login
) {
    val backStack = rememberNavBackStack(startDestination)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Screen.Home)
                    },onNeedsPerfil = {
                        backStack.clear()
                        backStack.add(Screen.PerfilForm)
                    },
                    onNavigateToRegister = {
                        backStack.add(Screen.Register)
                    }
                )
            }

            entry<Screen.Register> {
                RegisterScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onRegisterSuccess = { backStack.removeLastOrNull() }
                )
            }

            entry<Screen.PerfilForm> {
                PerfilFormScreen(
                    onPerfilCompletado = {
                        backStack.clear()
                        backStack.add(Screen.Home)
                    }
                )
            }

            entry<Screen.Home> {
                val logoutViewModel: LogoutViewModel = hiltViewModel()
                HomeScreen(
                    onLogout = {
                        logoutViewModel.logout {
                            backStack.clear()
                            backStack.add(Screen.Login)
                        }
                    }
                )
            }
        }
    )
}
