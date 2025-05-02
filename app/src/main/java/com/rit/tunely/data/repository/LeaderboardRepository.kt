package com.rit.tunely.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.rit.tunely.data.model.User
import com.rit.tunely.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(private val db: FirebaseFirestore) {
    private val users get() = db.collection("users")
    suspend fun fetchTopUsers(limit: Long = 50): Resource<List<User>> = try {
        val snap = users.orderBy(
            "points",
            com.google.firebase.firestore.Query.Direction.DESCENDING
        ).limit(limit).get().await()
        Resource.Success(snap.toObjects(User::class.java))
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Leaderboard error")
    }
}
