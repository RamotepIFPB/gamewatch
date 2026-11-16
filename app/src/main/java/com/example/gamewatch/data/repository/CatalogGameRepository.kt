
package com.example.gamewatch.data.repository

import com.example.gamewatch.data.model.CatalogGame
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CatalogGameRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Acessar a coleção global de jogos
    private fun getGamesCollection(): CollectionReference {
        return firestore.collection("games")
    }

    // CREATE: Adicionar um jogo ao catálogo
    fun adicionarJogo(jogo: CatalogGame): Task<Void> {
        val documento = getGamesCollection().document()

        val novoJogo = jogo.copy(
            id = documento.id
        )

        return documento.set(novoJogo)
    }

    // READ: Listar os jogos do catálogo
    fun listarJogos(): Task<List<CatalogGame>> {
        return getGamesCollection()
            .get()
            .continueWith { tarefa ->
                if (!tarefa.isSuccessful) {
                    throw tarefa.exception
                        ?: Exception("Erro ao buscar jogos.")
                }

                tarefa.result.documents.mapNotNull { documento ->
                    documento.toObject(CatalogGame::class.java)?.copy(
                        id = documento.id
                    )
                }
            }
    }

    // UPDATE: Atualizar um jogo
    fun atualizarJogo(jogo: CatalogGame): Task<Void> {
        return getGamesCollection()
            .document(jogo.id)
            .set(jogo)
    }

    // DELETE: Excluir um jogo
    fun excluirJogo(jogoId: String): Task<Void> {
        return getGamesCollection()
            .document(jogoId)
            .delete()
    }
}


