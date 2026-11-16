package com.example.gamewatch.data.model

data class Game(
    val id: String = "",
    val nome: String = "",
    val desenvolvedora: String = "",
    val precoAtual: Double = 0.0,
    val precoDesejado: Double = 0.0,
    val imagemUrl: String = "",
    val estaEmPromocao: Boolean = false
)


