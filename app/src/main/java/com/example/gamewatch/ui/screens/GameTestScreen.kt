package com.example.gamewatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamewatch.data.model.Game
import com.example.gamewatch.data.repository.GameRepository

@Composable
fun GameTestScreen() {

    val repository = remember { GameRepository() }

    var nome by remember { mutableStateOf("") }
    var desenvolvedora by remember { mutableStateOf("") }
    var precoAtual by remember { mutableStateOf("") }
    var precoDesejado by remember { mutableStateOf("") }

    var jogos by remember { mutableStateOf<List<Game>>(emptyList()) }
    var mensagem by remember { mutableStateOf("") }

    var jogoEmEdicao by remember { mutableStateOf<String?>(null) }
    var carregando by remember { mutableStateOf(false) }

    fun carregarJogos() {
        carregando = true

        repository.listarJogos()
            .addOnSuccessListener { resultado ->
                jogos = resultado
                carregando = false
            }
            .addOnFailureListener { erro ->
                mensagem = "Erro ao listar: ${erro.message}"
                carregando = false
            }
    }

    fun limparCampos() {
        nome = ""
        desenvolvedora = ""
        precoAtual = ""
        precoDesejado = ""
        jogoEmEdicao = null
    }

    fun salvarJogo() {
        val atual = precoAtual.replace(",", ".").toDoubleOrNull()
        val desejado = precoDesejado.replace(",", ".").toDoubleOrNull()

        if (nome.isBlank() || desenvolvedora.isBlank() ||
            atual == null || desejado == null
        ) {
            mensagem = "Preencha todos os campos corretamente!"
            return
        }

        val jogo = Game(
            id = jogoEmEdicao ?: "",
            nome = nome,
            desenvolvedora = desenvolvedora,
            precoAtual = atual,
            precoDesejado = desejado,
            imagemUrl = "",
            estaEmPromocao = false
        )

        carregando = true

        val operacao = if (jogoEmEdicao == null) {
            repository.adicionarJogo(jogo)
        } else {
            repository.atualizarJogo(jogo)
        }

        operacao
            .addOnSuccessListener {
                mensagem = if (jogoEmEdicao == null) {
                    "Jogo adicionado com sucesso!"
                } else {
                    "Jogo atualizado com sucesso!"
                }

                limparCampos()
                carregarJogos()
            }
            .addOnFailureListener { erro ->
                mensagem = "Erro ao salvar: ${erro.message}"
                carregando = false
            }
    }

    LaunchedEffect(Unit) {
        carregarJogos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "GameWatch - Teste CRUD",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = if (jogoEmEdicao == null)
                "Adicionar jogo"
            else
                "Editar jogo",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do jogo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = desenvolvedora,
            onValueChange = { desenvolvedora = it },
            label = { Text("Desenvolvedora") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precoAtual,
            onValueChange = { precoAtual = it },
            label = { Text("Preço atual (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precoDesejado,
            onValueChange = { precoDesejado = it },
            label = { Text("Preço desejado (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { salvarJogo() },
            enabled = !carregando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (jogoEmEdicao == null)
                    "Adicionar jogo"
                else
                    "Salvar alterações"
            )
        }

        if (jogoEmEdicao != null) {
            OutlinedButton(
                onClick = { limparCampos() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar edição")
            }
        }

        if (mensagem.isNotBlank()) {
            Text(
                text = mensagem,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider()

        Text(
            text = "Jogos cadastrados",
            style = MaterialTheme.typography.titleLarge
        )

        if (carregando) {
            CircularProgressIndicator()
        }

        if (jogos.isEmpty() && !carregando) {
            Text("Nenhum jogo cadastrado.")
        }

        jogos.forEach { jogo ->

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = jogo.nome,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text("Desenvolvedora: ${jogo.desenvolvedora}")

                    Text("Preço atual: R$ ${jogo.precoAtual}")

                    Text("Preço desejado: R$ ${jogo.precoDesejado}")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Button(
                            onClick = {
                                nome = jogo.nome
                                desenvolvedora = jogo.desenvolvedora
                                precoAtual = jogo.precoAtual.toString()
                                precoDesejado = jogo.precoDesejado.toString()
                                jogoEmEdicao = jogo.id
                                mensagem = "Editando ${jogo.nome}"
                            }
                        ) {
                            Text("Editar")
                        }

                        OutlinedButton(
                            onClick = {
                                carregando = true

                                repository.excluirJogo(jogo.id)
                                    .addOnSuccessListener {
                                        mensagem = "Jogo excluído!"
                                        carregarJogos()
                                    }
                                    .addOnFailureListener { erro ->
                                        mensagem = "Erro ao excluir: ${erro.message}"
                                        carregando = false
                                    }
                            }
                        ) {
                            Text("Excluir")
                        }
                    }
                }
            }
        }
    }
}


