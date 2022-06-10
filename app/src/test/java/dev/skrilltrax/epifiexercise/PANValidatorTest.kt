package dev.skrilltrax.epifiexercise

import dev.skrilltrax.epifiexercise.validation.PANValidator
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PANValidatorTest {
    private val panValidator = PANValidator()

    @Test
    fun testCorrectPANNumberIsValid() {
        val correctPan = "ABHJW0000D"
        val isValid = panValidator.isValid(correctPan)

        assertTrue(isValid)
    }

    @Test
    fun testIncorrectPANNumberIsInvalid() {
        val correctPan = "AZZZW0000D"
        val isValid = panValidator.isValid(correctPan)

        assertFalse(isValid)
    }

    @Test
    fun testEmptyPANNumberIsInvalid() {
        val correctPan = ""
        val isValid = panValidator.isValid(correctPan)

        assertFalse(isValid)
    }

    @Test
    fun testSpaceInPANNumberIsInvalid() {
        val correctPan = "ABHJ 0000D"
        val isValid = panValidator.isValid(correctPan)

        assertFalse(isValid)
    }

    @Test
    fun testShortPANNumberIsInvalid() {
        val correctPan = "ABHJW000D"
        val isValid = panValidator.isValid(correctPan)

        assertFalse(isValid)
    }

    @Test
    fun testLongPANNumberIsInvalid() {
        val correctPan = "ABHJW0000DD"
        val isValid = panValidator.isValid(correctPan)

        assertFalse(isValid)
    }
}