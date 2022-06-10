package dev.skrilltrax.epifiexercise.bank

data class BankUiState(
  val isPANValid: Boolean = true,
  val isDateValid: Boolean = true,
  val isNextButtonEnabled: Boolean = false
)
