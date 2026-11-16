package com.example.gamewatch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gamewatch.data.model.CheapSharkDeal
import com.example.gamewatch.data.repository.CheapSharkRepository
import java.util.Locale

@Composable
fun CheapSharkTestScreen(
    onVoltar: () -> Unit
) {
    val repository = remember { CheapSharkRepository() }

    var ofertas by remember {
        mutableStateOf<List<CheapSharkDeal>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    var tentativa by remember {
        mutableStateOf(0)
    }

    // Busca as ofertas da CheapShark
    LaunchedEffect(tentativa) {
        carregando = true
        erro = null

        try {
            ofertas = repository.buscarOfertas()
        } catch (e: Exception) {
            erro = e.message ?: "Erro ao buscar ofertas."
        } finally {
            carregando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(onClick = onVoltar) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ofertas da CheapShark",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {

            carregando -> {
                CircularProgressIndicator()
                Text("Buscando ofertas...")
            }

            erro != null -> {
                Text(
                    text = "Erro: $erro",
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

            ofertas.isEmpty() -> {
                Text("Nenhuma oferta encontrada.")
            }

            else -> {
                Text("Ofertas encontradas: ${ofertas.size}")

                Spacer(modifier = Modifier.height(8.dp))

                // Grade adaptável para celulares e tablets
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(
                        minSize = 280.dp
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = ofertas,
                        key = { it.dealID }
                    ) { oferta ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                AsyncImage(
                                    model = oferta.thumb,
                                    contentDescription = "Imagem de ${oferta.title}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16f / 9f),
                                    contentScale = ContentScale.Fit
                                )

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                Text(
                                    text = oferta.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 2
                                )

                                Spacer(
                                    modifier = Modifier.height(6.dp)
                                )

                                val precoAtual =
                                    oferta.salePrice.toDoubleOrNull() ?: 0.0

                                val precoOriginal =
                                    oferta.normalPrice.toDoubleOrNull() ?: 0.0

                                val desconto =
                                    oferta.savings.toDoubleOrNull() ?: 0.0

                                Text(
                                    text = "Preço atual: ${
                                        String.format(
                                            Locale.US,
                                            "US$ %.2f",
                                            precoAtual
                                        )
                                    }"
                                )

                                Text(
                                    text = "Preço original: ${
                                        String.format(
                                            Locale.US,
                                            "US$ %.2f",
                                            precoOriginal
                                        )
                                    }"
                                )

                                Text(
                                    text = "Desconto: ${
                                        String.format(
                                            Locale.US,
                                            "%.1f%%",
                                            desconto
                                        )
                                    }",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



