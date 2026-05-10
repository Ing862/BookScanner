package com.ingwoj.bookscanner.userServices.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingwoj.bookscanner.data.User
import com.ingwoj.bookscanner.userServices.EmailValidator
import com.ingwoj.bookscanner.userServices.NameValidator
import com.ingwoj.bookscanner.userServices.PasswordValidator
import com.ingwoj.bookscanner.userServices.UsernameValidator
import com.ingwoj.bookscanner.userServices.YearOfBirthValidator
import com.ingwoj.bookscanner.services.AuthService
import com.ingwoj.bookscanner.userServices.PasswordConditions
import com.ingwoj.bookscanner.userServices.RegistrationFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserRegistrationViewModel() : ViewModel() {

    // Auth
    private val auth: AuthService by lazy {
        AuthService()
    }

    private val _registrationFormState = MutableStateFlow(RegistrationFormState())
    val registrationFormState = _registrationFormState.asStateFlow()

    private val _uiState = MutableStateFlow<UIRegistrationState>(UIRegistrationState.Idle)
    val uiState = _uiState.asStateFlow()

    /**
     * Validates user's name on change, updates isButtonEnabled parameter
     * @param name user's name
     */
    fun validateNameOnChange(name: String){
        _registrationFormState.update { current ->
            val error = NameValidator.validate(name)
            val updated = current.copy(
                data = current.data.copy(name = name),
                errors = current.errors.copy(nameError = error)
            )
            updated.copy(
                isButtonEnabled = checkValidationData(updated)
            )
        }
    }

    /**
     * Validates user's username on change, updates isButtonEnabled parameter
     * @param username user's username
     */
    fun validateUsernameOnChange(username: String){
        _registrationFormState.update { current ->
            val error = UsernameValidator.validate(username)
            val updated = current.copy(
                data = current.data.copy(username = username),
                errors = current.errors.copy(usernameError = error)
            )
            updated.copy(
                isButtonEnabled = checkValidationData(updated)
            )
        }
    }

    /**
     * Validates user's year of birth on change, updates isButtonEnabled parameter
     * @param year user's year of birth
     */
    fun validateYearOnChange(year: String){
        _registrationFormState.update { current ->
            val error = YearOfBirthValidator.validate(year)
            val updated = current.copy(
                data = current.data.copy(yearOfBirth = year),
                errors = current.errors.copy(yearOfBirthError = error)
            )
            updated.copy(
                isButtonEnabled = checkValidationData(updated)
            )
        }
    }

    /**
     * Validates user's email on change, updates isButtonEnabled parameter
     * @param email user's email
     */
    fun validateEmailOnChange(email: String){
        _registrationFormState.update { current ->
            val error = EmailValidator.validate(email)
            val updated = current.copy(
                data = current.data.copy(email = email),
                errors = current.errors.copy(emailError = error)
            )
            updated.copy(
                isButtonEnabled = checkValidationData(updated)
            )
        }
    }

    /**
     * Validates user's set password on change, checks if password meets the conditions, updates isButtonEnabled parameter
     * @param password user's password
     */
    fun validatePasswordOnChange(password: String) {
        _registrationFormState.update { current ->
            val passwordConditions = PasswordConditions(
                minLength = password.length >= 8,
                hasUppercaseLetter = password.any { it.isUpperCase() },
                hasDigit = password.any { it.isDigit() },
                hasSpecialCharacter = password.any { !it.isLetterOrDigit() }
            )

            val error = PasswordValidator.validate(password)
            val updated = current.copy(
                data = current.data.copy(password = password),
                errors = current.errors.copy(passwordError = error),
                passwordConditions = passwordConditions
            )

            updated.copy(
                isButtonEnabled = checkValidationPassword(updated)
            )
        }
    }

    /**
     * Checks requirements for enabling the "next" button - if:
     * - inputs are not empty
     * - there are no input errors
     * @param currentState current state of the form
     */
    private fun checkValidationData(currentState: RegistrationFormState): Boolean{
        val noErrors = with(currentState.errors){
            nameError == null &&
            usernameError == null &&
            emailError == null &&
            yearOfBirthError == null &&
            passwordError == null
        }

        val inputNotEmpty = with(currentState.data){
            name.isNotEmpty() &&
            username.isNotEmpty() &&
            yearOfBirth.isNotEmpty() &&
            email.isNotEmpty()
        }

        return noErrors && inputNotEmpty
    }

    /**
     * Checks current state of the password (for enabling the "next" button)
     */
    fun checkValidationPassword(currentState: RegistrationFormState): Boolean{
        val conditionsMet = with (currentState.passwordConditions){
            minLength &&
            hasUppercaseLetter &&
            hasDigit &&
            hasSpecialCharacter
        }

        return conditionsMet
    }

    /**
     * Disables the "next" button
     */
    fun disableButton(){
        _registrationFormState.update {
            it.copy(isButtonEnabled = false)
        }
    }

    // TODO: register user in firebase
    // TODO: error control in registration
    // Register in firebase
    // jeżeli warunki spełnione to korzysta z serwisu firebase
    fun registerUser(email: String, password: String, context: Context) {
        val value = registrationFormState.value
        val user = User(
            firstName = value.data.name,
            username = value.data.username,
            dateOfBirth = value.data.yearOfBirth.toInt()
        )

        viewModelScope.launch{
            _uiState.value = UIRegistrationState.Loading
            val result = auth.registerUser(user, email, password, context)

            when(result){
                is AuthService.AuthResult.Success -> {
                    _uiState.value = UIRegistrationState.Success
                }
                is AuthService.AuthResult.Error -> {
                    _uiState.value = UIRegistrationState.Error(result.message)
                }
            }
        }
    }

    fun resetPasswordValidation() {
        _registrationFormState.value = RegistrationFormState(
            passwordConditions = PasswordConditions(
                minLength = false,
                hasUppercaseLetter = false,
                hasDigit = false,
                hasSpecialCharacter = false
            )
        )
    }

    // TODO: add user to database

    sealed class UIRegistrationState{
        object Idle: UIRegistrationState()
        object Loading: UIRegistrationState()
        object Success: UIRegistrationState()
        data class Error(val message: String): UIRegistrationState()
    }

}