package dev.skrilltrax.epifiexercise.validation

import java.util.regex.Pattern

/*
 * A class to validate PAN number received from user input.
 */
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
