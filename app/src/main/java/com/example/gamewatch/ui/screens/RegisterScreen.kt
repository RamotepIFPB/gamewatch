
package com.example.gamewatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamewatch.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var mostrarSenha by rememberSaveable { mutableStateOf(false) }
    var mostrarConfirmacao by rememberSaveable { mutableStateOf(false) }
    var erroValidacao by remember { mutableStateOf<String?>(null) }

    val uiState = viewModel.uiState
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onRegisterSuccess()
        }
    }

    // Fundo ocupa a tela inteira e mantém o formulário centralizado.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF171717)),
        contentAlignment = Alignment.Center
    ) {

        // Limita a largura do formulário em telas maiores.
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "GAMEWATCH",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00D4AA)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crie sua conta e acompanhe seus jogos.",
                color = Color.LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Criar conta",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("E-mail") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00D4AA),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF00D4AA),
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color(0xFF00D4AA)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senha") },
                singleLine = true,
                visualTransformation = if (mostrarSenha)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    TextButton(
                        onClick = {
                            mostrarSenha = !mostrarSenha
                        }
                    ) {
                        Text(
                            if (mostrarSenha) "Ocultar" else "Mostrar",
                            color = Color(0xFF00D4AA)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00D4AA),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF00D4AA),
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color(0xFF00D4AA)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar senha") },
                singleLine = true,
                visualTransformation = if (mostrarConfirmacao)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    TextButton(
                        onClick = {
                            mostrarConfirmacao = !mostrarConfirmacao
                        }
                    ) {
                        Text(
                            if (mostrarConfirmacao) "Ocultar" else "Mostrar",
                            color = Color(0xFF00D4AA)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00D4AA),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF00D4AA),
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color(0xFF00D4AA)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            val mensagemErro = erroValidacao ?: uiState.error

            if (mensagemErro != null) {
                Text(
                    text = mensagemErro,
                    color = Color(0xFFFF6B6B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    erroValidacao = null

                    when {
                        senha.length < 6 -> {
                            erroValidacao =
                                "A senha deve ter pelo menos 6 caracteres."
                        }

                        senha != confirmarSenha -> {
                            erroValidacao = "As senhas não são iguais."
                        }

                        else -> {
                            viewModel.cadastrar(email.trim(), senha)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isLoading &&
                        email.isNotBlank() &&
                        senha.isNotBlank() &&
                        confirmarSenha.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00D4AA),
                    contentColor = Color.Black
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Cadastrar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Já possui uma conta?",
                    color = Color.LightGray
                )

                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Entrar",
                        color = Color(0xFF00D4AA),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


