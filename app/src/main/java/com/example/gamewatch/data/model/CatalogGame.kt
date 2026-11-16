
package com.example.gamewatch.data.model

data class CatalogGame(
    val id: String = "",
    val nome: String = "",
    val desenvolvedora: String = "",
    val descricao: String = "",
    val precoAtual: Double = 0.0,
    val precoOriginal: Double = 0.0,
    val imagemUrl: String = "",
    val estaEmPromocao: Boolean = false
)


