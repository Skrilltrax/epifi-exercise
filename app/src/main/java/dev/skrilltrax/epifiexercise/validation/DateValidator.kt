package dev.skrilltrax.epifiexercise.validation

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateValidator : TextValidator {

  override fun isValid(text: String): Boolean {
    val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    simpleDateFormat.isLenient = false
    try {
      val birthDate = simpleDateFormat.parse(text) ?: return false
      val currentDate = Date()
      if (birthDate.after(currentDate)) return false

      val calendar = Calendar.getInstance().apply { time = currentDate }
      calendar.add(Calendar.YEAR, -MAX_AGE)

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
