package com.example.gamewatch.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Realizar login
    fun login(email: String, senha: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, senha)
    }

    // Criar uma nova conta
    fun cadastrar(email: String, senha: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, senha)
    }

    // Realizar logout
    fun logout() {
        firebaseAuth.signOut()
    }

    // Verificar usuário atualmente autenticado
    fun usuarioAtual() = firebaseAuth.currentUser
}
