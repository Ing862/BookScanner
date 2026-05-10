package com.ingwoj.bookscanner.repository

import com.ingwoj.bookscanner.data.User
import com.ingwoj.bookscanner.firestore.UserDataSource

class UserRepository(
    private val dataSource: UserDataSource
) {
    suspend fun addUser(user: User): Boolean {
        return try {
            dataSource.addUser(user)
            true
        }
        catch ( e: Exception) {
            false
        }
    }

}