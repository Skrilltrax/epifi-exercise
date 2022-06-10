package dev.skrilltrax.epifiexercise.validation

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/*
 * A class to validate dates received from user input.
 * This class expects the date to be in dd/MM/yyyy format.
 *
 * Conditions where date is valid:
 * 1. Date is not older than 150 years from today
 * 2. Date is not in the future
 * 3. Date string is not properly formatted
 */
class DateValidator : TextValidator {

  override fun isValid(text: String): Boolean {
    val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    simpleDateFormat.isLenient = false
    try {
      val birthDate = simpleDateFormat.parse(text) ?: return false
      val currentDate = Date()
      // If date is in future, return false
      if (birthDate.after(currentDate)) return false

      val calendar = Calendar.getInstance().apply { time = currentDate }
      calendar.add(Calendar.YEAR, -MAX_AGE)

      // If date is more than 150 years old, return false
      val minDate = calendar.time
      if (birthDate.before(minDate)) return false
    } catch (e: ParseException) {
      return false
    }

    return true
  }

  companion object {
    const val DATE_PATTERN: String = "dd/MM/yyyy"
    const val MAX_AGE: Int = 150
    const val DAY_LENGTH: Int = 2
    const val MONTH_LENGTH: Int = 2
    const val YEAR_LENGTH: Int = 4
  }
}
