package dev.skrilltrax.epifiexercise.validation

import java.util.regex.Pattern

class PANValidator : TextValidator {
  override fun isValid(text: String): Boolean {
    val matcher = regexPattern.matcher(text)
    return matcher.matches()
  }

  companion object {
    val regexPattern: Pattern = Pattern.compile("[A-Z]{3}[ABCFGHJLPT][A-Z][0-9]{4}[A-Z]")
    const val PAN_LENGTH: Int = 10
  }
}
