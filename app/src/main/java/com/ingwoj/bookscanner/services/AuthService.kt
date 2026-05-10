package com.ingwoj.bookscanner.services

import android.content.Context
import android.util.Log
import com.ingwoj.bookscanner.R
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.ingwoj.bookscanner.data.User
import com.ingwoj.bookscanner.firestore.UserDataSource
import com.ingwoj.bookscanner.repository.UserRepository
import kotlinx.coroutines.tasks.await

class AuthService() {
    private val auth: FirebaseAuth = Firebase.auth

    // TODO: to chyba nie powinno tu być ale spoko
    private val db = FirebaseFirestore.getInstance()
    private val data = UserDataSource(db)
    private val repo = UserRepository(data)

    // TODO: logging and checking if signUp was done

    /**
     * Results for firebase.auth
     * - success with message
     * - error with exception
     */
    sealed class AuthResult {
        object Success: AuthResult()
        data class Error(val message: String): AuthResult()
    }

    /**
     * Registers user in firebase, saves user data in firestore
     *
     * @param user User class to be saved in firestore
     * @param email User's email
     * @param password User's password
     *
     * @exception FirebaseAuthException "Registration failure - authentication error"
     * @exception FirebaseNetworkException "Registration failure - network error"
     */
    suspend fun registerUser(user: User, email: String, password: String, context: Context): AuthResult {
        return try{
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("User is null")
            val newUser = user.copy(userId = uid)
            repo.addUser(newUser)
            AuthResult.Success
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthException -> {
                    Log.d("AuthError", "errorCode = ${e.errorCode}, message = ${e.message}")
                    if (e.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") AuthResult.Error(context.getString(R.string.registerErrUsedEmail))
                    else AuthResult.Error(context.getString(R.string.regiserErrAuth))
                }
                is FirebaseNetworkException -> AuthResult.Error(context.getString(R.string.registerErrNetwork))
                else -> AuthResult.Error(context.getString(R.string.registerErrUnknown))
            }
        }
    }

    /**
     * Logs in user using Firebase Authentication.
     * On success function logs "Log in success"
     *
     * @param email the user's email address
     * @param password the user's password
     *
     * @exception FirebaseAuthException wrong password or user not found
     * @exception FirebaseNetworkException "Log in failure - network error"
     */
    suspend fun loginUser(email: String, password: String, context: Context): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthException -> {
                    Log.d("AuthError", "errorCode = ${e.errorCode}, message = ${e.message}")
                    when (e.errorCode) {
                        "ERROR_INVALID_CREDENTIAL" -> AuthResult.Error(context.getString(R.string.wrongPasswordOrEmail))
                        else -> AuthResult.Error(context.getString(R.string.authError))
                    }
                }
                is FirebaseNetworkException -> AuthResult.Error(context.getString(R.string.loginErrNetwork))
                else -> AuthResult.Error(context.getString(R.string.loginErrUnknown))
            }
        }
    }

    /**
     * @return current logged in user - FirebaseUser
     */
    fun getLoggedUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Logs out user
     * @return current logged in user (FirebaseUser)
     */
    fun signOut(): FirebaseUser?{
        auth.signOut()
        return getLoggedUser()
    }

    // TODO: recover password in firebase

    suspend fun sendPassResetEmail(email: String, context: Context): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success
        }
        catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> AuthResult.Error(context.getString(R.string.passResetErrWrongFormatEmail))
                is FirebaseAuthInvalidCredentialsException -> AuthResult.Error(context.getString(R.string.passResetErrWrongFormatEmail))
                is FirebaseNetworkException -> AuthResult.Error(context.getString(R.string.passResetErrNetwork))
                is FirebaseAuthException -> AuthResult.Error(context.getString(R.string.passResetErrAuthErr))
                else -> AuthResult.Error(context.getString(R.string.passResetErrUnknown))
            }
        }
    }
}