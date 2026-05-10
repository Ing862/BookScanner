package com.ingwoj.bookscanner.userServices

/**
 * User registration data
 */
data class RegistrationData(
    val name: String,
    val username: String,
    val email: String,
    val yearOfBirth: String,
    val password: String

)

/**
 * Types of errors in user data (registration)
 */
data class UserDataErrors(
    val nameError: Int? = null,
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val yearOfBirthError: Int? = null,
    val passwordError: Int? = null
)

/**
 * Conditions for every password
 */
data class PasswordConditions(
    val minLength: Boolean = false,
    val hasUppercaseLetter: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialCharacter: Boolean = false
)

data class RegistrationFormState(
    val data: RegistrationData = RegistrationData("", "", "", "", ""),
    val errors: UserDataErrors = UserDataErrors(),
    val passwordConditions: PasswordConditions = PasswordConditions(),
    val isButtonEnabled: Boolean = false
)

// User login
data class LoginFormState (
    val email: String ="",
    val password: String ="",

    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isAllValid: Boolean = false,
)
