package com.ingwoj.bookscanner.userServices

import com.ingwoj.bookscanner.R
import java.util.Calendar

// Int because R.string is an int number
interface Validator {
    fun validate(value: String): Int?
}

object NameValidator : Validator {
    override fun validate(value: String): Int? =
        if (value.isEmpty()) R.string.nameIsEmptyError else null
}

object UsernameValidator : Validator {
    override fun validate(value: String): Int? =
        if (value.isEmpty()) R.string.usernameIsEmptyError else null
}

object YearOfBirthValidator: Validator {
    override fun validate(value: String): Int? {
        val error = when {
            value.isEmpty() -> R.string.yearIsEmptyError
            value.length < 4 -> R.string.yearIncorrectFormat
            value.toInt() >= Calendar.getInstance().get(Calendar.YEAR) -> R.string.yearIncorrectFormat
            else -> null
        }
        return error
    }
}

object EmailValidator: Validator {
    override fun validate(value: String): Int? {
        val error = when {
            value.isEmpty() -> R.string.emailIsEmptyError
            !(value.contains('@') && value.contains('.') &&
                    (value.indexOf('.') != value.length-1)) -> R.string.emailIncorrectFormat
            else -> null
        }
        return error
    }
}

object PasswordValidator: Validator {
    override fun validate(value: String): Int? =
        if (value.isEmpty()) R.string.passwordIsEmptyError else null
}