package com.ingwoj.bookscanner.userServices.login

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingwoj.bookscanner.userServices.EmailValidator
import com.ingwoj.bookscanner.userServices.PasswordValidator
import com.ingwoj.bookscanner.services.AuthService
//import com.ingwoj.bookscanner.userServices.AuthViewModel
import com.ingwoj.bookscanner.userServices.LoginFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {

    private val auth: AuthService by lazy {
        AuthService()
    }

    /**
     * State flow for user's input
     */
    private val _formState = MutableStateFlow(LoginFormState())
    val formState = _formState.asStateFlow()

    /**
     * State of the UI: Loading, Success or Error
     */
    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    /**
     * Validates email on change by the user
     * @param email user's input email
     */
    fun validateEmailOnChange(email: String) {
        _formState.update { current ->
            val error = EmailValidator.validate(email)
            current.copy(
                email = email,
                emailError = error,
                isAllValid = error == null &&
                        current.passwordError == null &&
                        email.isNotBlank() &&
                        current.password.isNotBlank()
            )
        }
    }

    /**
     * Validates password on change by the user
     * @param password user's input password
     */
    fun validatePasswordOnChange(password: String) {
        _formState.update { current ->
            val error = PasswordValidator.validate(password)
            current.copy(
                password = password,
                passwordError = error,
                isAllValid = current.emailError == null &&
                        error == null &&
                        current.email.isNotBlank() &&
                        password.isNotBlank()
            )
        }
    }
    /**
     * Logs in user
     */
    fun logInUser(context: Context) {
        viewModelScope.launch{
            _uiState.value = UIState.Loading
            val result = auth.loginUser(_formState.value.email, _formState.value.password, context)

            when(result){
                is AuthService.AuthResult.Success -> {
                    _uiState.value = UIState.Success
                }
                is AuthService.AuthResult.Error -> {
                    _uiState.value = UIState.Error(result.message)
                }
            }

        }
    }

    fun sendResetEmail(context: Context) {
        viewModelScope.launch{
            _uiState.value = UIState.Loading

            val result = auth.sendPassResetEmail(_formState.value.email, context)

            when(result){
                is AuthService.AuthResult.Success -> {
                    _uiState.value = UIState.ResetEmailSuccess
                }
                is AuthService.AuthResult.Error -> {
                    _uiState.value = UIState.Error(result.message)
                }
            }
        }
    }

    sealed class UIState{
        object Idle: UIState()
        object Loading: UIState()
        object Success: UIState()
        data class Error(val message: String): UIState()
        // TODO: For success sending password reset email
        object ResetEmailSuccess: UIState()
    }


    // TODO: auth sprawdza czy użytkownik jest zalogowany, zwraca błąd lub potwierdzenie

}