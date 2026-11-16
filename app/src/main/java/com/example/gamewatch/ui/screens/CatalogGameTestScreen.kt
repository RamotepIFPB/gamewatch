
package com.example.gamewatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gamewatch.data.model.CatalogGame
import com.example.gamewatch.data.repository.CatalogGameRepository
import androidx.compose.runtime.LaunchedEffect

@Composable
fun CatalogGameTestScreen() {

    val repository = remember { CatalogGameRepository() }

    var jogos by remember { mutableStateOf<List<CatalogGame>>(emptyList()) }

    var nome by remember { mutableStateOf("") }
    var desenvolvedora by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var precoAtual by remember { mutableStateOf("") }
    var precoOriginal by remember { mutableStateOf("") }
    var imagemUrl by remember { mutableStateOf("") }
    var estaEmPromocao by remember { mutableStateOf(false) }

    var jogoEmEdicao by remember {
        mutableStateOf<CatalogGame?>(null)
    }

    var mensagem by remember { mutableStateOf("") }
    var carregando by remember { mutableStateOf(false) }

    fun limparFormulario() {
        nome = ""
        desenvolvedora = ""
        descricao = ""
        precoAtual = ""
        precoOriginal = ""
        imagemUrl = ""
        estaEmPromocao = false
        jogoEmEdicao = null
    }

    fun carregarJogos() {
        carregando = true

        repository.listarJogos()
            .addOnSuccessListener { lista ->
                jogos = lista
                carregando = false
            }
            .addOnFailureListener { erro ->
                mensagem = "Erro ao carregar jogos: ${erro.message}"
                carregando = false
            }
    }

    fun salvarJogo() {

        val atual = precoAtual.replace(",", ".").toDoubleOrNull()
        val original = precoOriginal.replace(",", ".").toDoubleOrNull()

        if (
            nome.isBlank() ||
            desenvolvedora.isBlank() ||
            atual == null ||
            original == null ||
            atual < 0 ||
            original < 0
        ) {
            mensagem = "Preencha os campos obrigatórios corretamente!"
            return
        }

        val jogo = CatalogGame(
            id = jogoEmEdicao?.id ?: "",
            nome = nome.trim(),
            desenvolvedora = desenvolvedora.trim(),
            descricao = descricao.trim(),
            precoAtual = atual,
            precoOriginal = original,
            imagemUrl = imagemUrl.trim(),
            estaEmPromocao = estaEmPromocao
        )

        val operacao = if (jogoEmEdicao == null) {
            repository.adicionarJogo(jogo)
        } else {
            repository.atualizarJogo(jogo)
        }

        operacao
            .addOnSuccessListener {
                mensagem = if (jogoEmEdicao == null) {
                    "Jogo cadastrado com sucesso!"
                } else {
                    "Jogo atualizado com sucesso!"
                }

                limparFormulario()
                carregarJogos()
            }
            .addOnFailureListener { erro ->
                mensagem = "Erro ao salvar jogo: ${erro.message}"
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
            text = "GameWatch - Gerenciar catálogo",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = if (jogoEmEdicao == null) {
                "Cadastrar novo jogo"
            } else {
                "Editar jogo"
            },
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do jogo *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = desenvolvedora,
            onValueChange = { desenvolvedora = it },
            label = { Text("Desenvolvedora *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = precoAtual,
            onValueChange = { precoAtual = it },
            label = { Text("Preço atual (R$) *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = precoOriginal,
            onValueChange = { precoOriginal = it },
            label = { Text("Preço original (R$) *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = imagemUrl,
            onValueChange = { imagemUrl = it },
            label = { Text("URL da imagem (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(
                checked = estaEmPromocao,
                onCheckedChange = { estaEmPromocao = it }
            )

            Text("Jogo está em promoção")
        }

        Button(
            onClick = { salvarJogo() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (jogoEmEdicao == null) {
                    "Adicionar jogo"
                } else {
                    "Salvar alterações"
                }
            )
        }

        if (jogoEmEdicao != null) {
            OutlinedButton(
                onClick = {
                    limparFormulario()
                    mensagem = "Edição cancelada."
                },
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
            text = "Jogos cadastrados (${jogos.size})",
            style = MaterialTheme.typography.titleLarge
        )

        if (carregando) {
            CircularProgressIndicator()
        } else if (jogos.isEmpty()) {
            Text("Nenhum jogo cadastrado.")
        } else {

            jogos.forEach { jogo ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = jogo.nome,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Desenvolvedora: ${jogo.desenvolvedora}")

                        Text("Descrição: ${jogo.descricao}")

                        Text("Preço atual: R$ ${jogo.precoAtual}")

                        Text("Preço original: R$ ${jogo.precoOriginal}")

                        Text(
                            if (jogo.estaEmPromocao) {
                                "Em promoção"
                            } else {
                                "Sem promoção"
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Button(
                                onClick = {
                                    nome = jogo.nome
                                    desenvolvedora = jogo.desenvolvedora
                                    descricao = jogo.descricao
                                    precoAtual = jogo.precoAtual.toString()
                                    precoOriginal = jogo.precoOriginal.toString()
                                    imagemUrl = jogo.imagemUrl
                                    estaEmPromocao = jogo.estaEmPromocao
                                    jogoEmEdicao = jogo

                                    mensagem = "Editando ${jogo.nome}"
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Editar")
                            }

                            OutlinedButton(
                                onClick = {
                                    repository.excluirJogo(jogo.id)
                                        .addOnSuccessListener {
                                            mensagem = "${jogo.nome} excluído!"
                                            if (jogoEmEdicao?.id == jogo.id) {
                                                limparFormulario()
                                            }
                                            carregarJogos()
                                        }
                                        .addOnFailureListener { erro ->
                                            mensagem = "Erro ao excluir: ${erro.message}"
                                        }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Excluir")
                            }
                        }
                    }
                }
            }
        }
    }
}
