package com.rit.tunely.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.rit.tunely.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {
    suspend fun login(email: String, pwd: String): Resource<Unit> = try {
        auth.signInWithEmailAndPassword(email, pwd).await(); Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Login failed")
    }

    suspend fun signUp(email: String, pwd: String): Resource<Unit> = try {
        auth.createUserWithEmailAndPassword(email, pwd).await(); Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Sign-up failed")
    }

    fun currentUser() = auth.currentUser
    fun logout() = auth.signOut()
}
