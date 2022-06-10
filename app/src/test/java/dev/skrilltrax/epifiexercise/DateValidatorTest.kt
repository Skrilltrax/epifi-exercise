package dev.skrilltrax.epifiexercise

import dev.skrilltrax.epifiexercise.validation.DateValidator
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class DateValidatorTest {
    private val dateValidator = DateValidator()

    @Test
    fun testCorrectDateIsValid() {
        val birthDate = "29/09/1999"
        val isValid = dateValidator.isValid(birthDate)
        assertTrue(isValid)
    }

    @Test
    fun testIncorrectDateIsInvalid() {
        val birthDate = "29/13/1999"
        val isValid = dateValidator.isValid(birthDate)
        assertFalse(isValid)
    }

    @Test
    fun testLeapYearDateIsValid() {
        val birthDate = "29/2/2016"
        val isValid = dateValidator.isValid(birthDate)
        assertTrue(isValid)
    }

    @Test
    fun testNonLeapYearDateIsInvalid() {
        val birthDate = "29/2/2016"
        val isValid = dateValidator.isValid(birthDate)
        assertTrue(isValid)
    }

    @Test
    fun testFutureDateIsInvalid() {
        val todayDate = Date()
        val calendar = Calendar.getInstance().apply { time = todayDate }
        calendar.add(Calendar.DATE, 1)
        val tomorrowDate = calendar.time

        val sdf = SimpleDateFormat(DateValidator.DATE_PATTERN)
        val dateString = sdf.format(tomorrowDate)

        val isValid = dateValidator.isValid(dateString)
        assertFalse(isValid)
    }

    @Test
    fun testPastDateIsInvalid() {
        // Date older than 150 years in invalid
        val todayDate = Date()
        val calendar = Calendar.getInstance().apply { time = todayDate }
        calendar.add(Calendar.YEAR, -DateValidator.MAX_AGE)
        val oldDate = calendar.time

        val sdf = SimpleDateFormat(DateValidator.DATE_PATTERN)
        val dateString = sdf.format(oldDate)

        val isValid = dateValidator.isValid(dateString)
        assertFalse(isValid)
    }

    @Test
    fun testPastDateInRangeIsValid() {
        // Date newer than 150 years ago in invalid
        val todayDate = Date()
        val calendar = Calendar.getInstance().apply { time = todayDate }
        calendar.add(Calendar.YEAR, -DateValidator.MAX_AGE)
        calendar.add(Calendar.DATE, 1)
        val oldDate = calendar.time

        val sdf = SimpleDateFormat(DateValidator.DATE_PATTERN)
        val dateString = sdf.format(oldDate)

        val isValid = dateValidator.isValid(dateString)
        assertTrue(isValid)
    }
}