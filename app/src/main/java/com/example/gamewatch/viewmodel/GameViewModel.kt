package com.example.gamewatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.gamewatch.data.model.Game
import com.example.gamewatch.data.repository.GameRepository

data class GameUiState(
    val jogos: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class GameViewModel : ViewModel() {

    private val repository = GameRepository()

    var uiState by mutableStateOf(GameUiState())
        private set

    // READ: Carregar os jogos
    fun carregarJogos() {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            successMessage = null
        )

        try {
            repository.listarJogos()
                .addOnSuccessListener { jogos ->

                    uiState = uiState.copy(
                        jogos = jogos,
                        isLoading = false
                    )
                }
                .addOnFailureListener { erro ->

                    uiState = uiState.copy(
                        isLoading = false,
                        error = erro.localizedMessage
                            ?: "Não foi possível carregar os jogos."
                    )
                }
        } catch (erro: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = erro.localizedMessage
                    ?: "Não foi possível carregar os jogos."
            )
        }
    }

    // CREATE: Adicionar um jogo
    fun adicionarJogo(jogo: Game) {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            successMessage = null
        )

        try {
            repository.adicionarJogo(jogo)
                .addOnSuccessListener {

                    uiState = uiState.copy(
                        isLoading = false,
                        successMessage = "Jogo adicionado com sucesso!"
                    )

                    carregarJogos()
                }
                .addOnFailureListener { erro ->

                    uiState = uiState.copy(
                        isLoading = false,
                        error = erro.localizedMessage
                            ?: "Não foi possível adicionar o jogo."
                    )
                }
        } catch (erro: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = erro.localizedMessage
                    ?: "Não foi possível adicionar o jogo."
            )
        }
    }

    // UPDATE: Atualizar um jogo
    fun atualizarJogo(jogo: Game) {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            successMessage = null
        )

        try {
            repository.atualizarJogo(jogo)
                .addOnSuccessListener {

                    uiState = uiState.copy(
                        isLoading = false,
                        successMessage = "Jogo atualizado com sucesso!"
                    )

                    carregarJogos()
                }
                .addOnFailureListener { erro ->

                    uiState = uiState.copy(
                        isLoading = false,
                        error = erro.localizedMessage
                            ?: "Não foi possível atualizar o jogo."
                    )
                }
        } catch (erro: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = erro.localizedMessage
                    ?: "Não foi possível atualizar o jogo."
            )
        }
    }

    // DELETE: Excluir um jogo
    fun excluirJogo(jogoId: String) {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            successMessage = null
        )

        try {
            repository.excluirJogo(jogoId)
                .addOnSuccessListener {

                    uiState = uiState.copy(
                        isLoading = false,
                        successMessage = "Jogo removido com sucesso!"
                    )

                    carregarJogos()
                }
                .addOnFailureListener { erro ->

                    uiState = uiState.copy(
                        isLoading = false,
                        error = erro.localizedMessage
                            ?: "Não foi possível excluir o jogo."
                    )
                }
        } catch (erro: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = erro.localizedMessage
                    ?: "Não foi possível excluir o jogo."
            )
        }
    }
}


