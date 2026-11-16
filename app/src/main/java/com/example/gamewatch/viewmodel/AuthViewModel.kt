
package com.example.gamewatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.gamewatch.data.repository.AuthRepository

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var uiState by mutableStateOf(AuthUiState())
        private set

    init {
        uiState = uiState.copy(
            isAuthenticated = repository.usuarioAtual() != null
        )
    }

    // Realizar login
    fun login(email: String, senha: String) {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            successMessage = null
        )

        repository.login(email, senha)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    uiState = uiState.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        successMessage = "Login realizado com sucesso!"
                    )
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = task.exception?.localizedMessage
                            ?: "Não foi possível realizar o login."
                    )
                }
            }
    }

    // Criar uma nova conta
    fun cadastrar(email: String, senha: String) {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            successMessage = null
        )

        repository.cadastrar(email, senha)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    uiState = uiState.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        successMessage = "Conta criada com sucesso!"
                    )
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = task.exception?.localizedMessage
                            ?: "Não foi possível criar a conta."
                    )
                }
            }
    }

    // Encerrar sessão
    fun logout() {
        repository.logout()

        uiState = AuthUiState(
            isAuthenticated = false
        )
    }
}
