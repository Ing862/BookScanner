package com.ingwoj.bookscanner.menuSections.userProfileSection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ingwoj.bookscanner.services.AuthService

class UserProfileViewModel: ViewModel() {

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    // Auth
    private val auth: AuthService by lazy {
        AuthService()
    }

    // Log out
    fun logOut(){

        // TODO: poprawić na poprawne mvvm
        auth.signOut()
    }
}