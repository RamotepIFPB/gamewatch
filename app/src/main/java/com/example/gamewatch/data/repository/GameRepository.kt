package com.example.gamewatch.data.repository

import com.example.gamewatch.data.model.Game
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference

class GameRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Acessar a lista de desejos do usuário autenticado
    private fun getWishlistCollection(): CollectionReference {
        val usuarioId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("Nenhum usuário autenticado.")

        return firestore
            .collection("users")
            .document(usuarioId)
            .collection("wishlist")
    }

    // CREATE: Adicionar um jogo
    fun adicionarJogo(jogo: Game): Task<Void> {
        val documento = getWishlistCollection().document()

        val novoJogo = jogo.copy(
            id = documento.id
        )

        return documento.set(novoJogo)
    }

    // READ: Listar os jogos
    fun listarJogos(): Task<List<Game>> {
        return getWishlistCollection()
            .get()
            .continueWith { tarefa ->

                if (!tarefa.isSuccessful) {
                    throw tarefa.exception
                        ?: Exception("Erro ao buscar jogos.")
                }

                tarefa.result.documents.mapNotNull { documento ->
                    documento.toObject(Game::class.java)?.copy(
                        id = documento.id
                    )
                }
            }
    }

    // UPDATE: Atualizar um jogo
    fun atualizarJogo(jogo: Game): Task<Void> {
        return getWishlistCollection()
            .document(jogo.id)
            .set(jogo)
    }

    // DELETE: Excluir um jogo
    fun excluirJogo(jogoId: String): Task<Void> {
        return getWishlistCollection()
            .document(jogoId)
            .delete()
    }
}


