package com.ingwoj.bookscanner.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ingwoj.bookscanner.data.User
import kotlinx.coroutines.tasks.await
import java.time.Year

class UserDataSource (
    private val db : FirebaseFirestore
) {
    /**
     * Adds/rewrites user in firestore
     * @param user user class
     */
    suspend fun addUser(user: User) {
        db.collection("users").document(user.userId).set(user).await()
    }


}

