
package com.example.gamewatch.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gamewatch.ui.screens.CatalogScreen
import com.example.gamewatch.ui.screens.CheapSharkTestScreen
import com.example.gamewatch.ui.screens.LoginScreen
import com.example.gamewatch.ui.screens.RegisterScreen
import com.example.gamewatch.viewmodel.AuthViewModel

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // Tela de login
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Tela de cadastro
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Tela inicial
        composable("home") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Bem-vindo ao GameWatch!"
                )

                Button(
                    onClick = {
                        navController.navigate("catalog")
                    }
                ) {
                    Text("Abrir catálogo de jogos")
                }

                // NOVO BOTÃO: Testar CheapShark
                Button(
                    onClick = {
                        navController.navigate("cheapshark")
                    }
                ) {
                    Text("Testar ofertas da CheapShark")
                }

                Button(
                    onClick = {
                        authViewModel.logout()

                        navController.navigate("login") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text("Sair da conta")
                }
            }
        }

        // Catálogo de jogos
        composable("catalog") {
            CatalogScreen(
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        // NOVA TELA: Ofertas da CheapShark
        composable("cheapshark") {
            CheapSharkTestScreen(
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }
    }
}
