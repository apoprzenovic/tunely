package com.rit.tunely.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rit.tunely.data.model.User
import com.rit.tunely.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(private val db: FirebaseFirestore) {
    private val users get() = db.collection("users")
    suspend fun getUser(uid: String): Resource<User> = try {
        val snap = users.document(uid).get().await()
        val user = snap.toObject(User::class.java)?.copy(uid = snap.id) ?: User(uid = uid)
        Resource.Success(user)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "User fetch error")
    }

    suspend fun updateUser(user: User): Resource<Unit> = try {
        users.document(user.uid).set(user).await(); Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Profile update error")
    }
}
