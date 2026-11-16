
package com.example.gamewatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.example.gamewatch.data.model.CatalogGame
import com.example.gamewatch.data.repository.CatalogGameRepository
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Locale
import coil.compose.AsyncImage

@Composable
fun CatalogScreen(onVoltar: () -> Unit) {

    val repository = remember { CatalogGameRepository() }

    var jogos by remember {
        mutableStateOf<List<CatalogGame>>(emptyList())
    }

    var pesquisa by remember {
        mutableStateOf("")
    }

    var somentePromocao by remember {
        mutableStateOf(false)
    }

    var ordem by remember {
        mutableStateOf("Recomendados")
    }

    var menuAberto by remember {
        mutableStateOf(false)
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    var mensagemErro by remember {
        mutableStateOf("")
    }

    var tentativa by remember {
        mutableIntStateOf(0)
    }

    // Carregar jogos do Firestore
    LaunchedEffect(tentativa) {
        try {
            carregando = true
            mensagemErro = ""

            jogos = repository.listarJogos().await()

        } catch (erro: Exception) {
            mensagemErro = "Erro ao carregar jogos: ${erro.message}"
        } finally {
            carregando = false
        }
    }

    // Pesquisa, filtros e ordenação
    val jogosFiltrados = remember(
        jogos,
        pesquisa,
        somentePromocao,
        ordem
    ) {
        val filtrados = jogos.filter { jogo ->

            val correspondePesquisa =
                jogo.nome.contains(pesquisa, ignoreCase = true)

            val correspondePromocao =
                !somentePromocao || jogo.estaEmPromocao

            correspondePesquisa && correspondePromocao
        }

        when (ordem) {
            "Menor preço" -> filtrados.sortedBy { it.precoAtual }
            "Maior preço" -> filtrados.sortedByDescending { it.precoAtual }
            "A - Z" -> filtrados.sortedBy { it.nome.lowercase() }
            else -> filtrados
        }
    }

    // Formatação de preços
    fun formatarPreco(preco: Double): String {
        return NumberFormat.getCurrencyInstance(
            Locale("pt", "BR")
        ).format(preco)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Botão para voltar à tela inicial
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVoltar
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar para a tela inicial"
                )
            }

            Text(
                text = "Voltar para o início",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Título
        Text(
            text = "GameWatch",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Explore seus jogos favoritos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de pesquisa
        OutlinedTextField(
            value = pesquisa,
            onValueChange = { pesquisa = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Pesquisar jogos") },
            placeholder = { Text("Digite o nome do jogo...") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filtros e ordenação
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            FilterChip(
                selected = somentePromocao,
                onClick = {
                    somentePromocao = !somentePromocao
                },
                label = {
                    Text("Em promoção")
                }
            )

            Box {
                OutlinedButton(
                    onClick = {
                        menuAberto = true
                    }
                ) {
                    Text("Ordenar: $ordem")
                }

                DropdownMenu(
                    expanded = menuAberto,
                    onDismissRequest = {
                        menuAberto = false
                    }
                ) {
                    listOf(
                        "Recomendados",
                        "Menor preço",
                        "Maior preço",
                        "A - Z"
                    ).forEach { opcao ->

                        DropdownMenuItem(
                            text = { Text(opcao) },
                            onClick = {
                                ordem = opcao
                                menuAberto = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quantidade de jogos
        Text(
            text = "${jogosFiltrados.size} jogos encontrados",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Estados da tela
        if (carregando) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else if (mensagemErro.isNotBlank()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = mensagemErro,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        tentativa++
                    }
                ) {
                    Text("Tentar novamente")
                }
            }

        } else if (jogosFiltrados.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum jogo encontrado.")
            }

        } else {

            // Grade adaptável para celulares e tablets
            LazyVerticalGrid(
                columns = GridCells.Adaptive(
                    minSize = 340.dp
                ),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    bottom = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = jogosFiltrados,
                    key = { jogo -> jogo.id }
                ) { jogo ->

                    // Cartão do jogo
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {

                            // Capa do jogo
                            if (jogo.imagemUrl.isNotBlank()) {

                                AsyncImage(
                                    model = jogo.imagemUrl,
                                    contentDescription = "Capa de ${jogo.nome}",
                                    modifier = Modifier
                                        .width(95.dp)
                                        .height(135.dp),
                                    contentScale = ContentScale.Fit
                                )

                            } else {

                                Surface(
                                    modifier = Modifier
                                        .width(95.dp)
                                        .height(135.dp),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Sem imagem",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            // Informações do jogo
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {

                                Text(
                                    text = jogo.nome,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = jogo.desenvolvedora,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                if (jogo.estaEmPromocao) {
                                    Text(
                                        text = "EM PROMOÇÃO",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                Text(
                                    text = formatarPreco(jogo.precoAtual),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Preço original: ${formatarPreco(jogo.precoOriginal)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (jogo.descricao.isNotBlank()) {
                                    Text(
                                        text = jogo.descricao,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

