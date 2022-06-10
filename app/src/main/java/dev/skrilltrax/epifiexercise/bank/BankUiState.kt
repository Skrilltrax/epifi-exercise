package dev.skrilltrax.epifiexercise.bank

data class BankUiState(
  val isPANValid: Boolean = false,
  val isDateValid: Boolean = false,
  val showPANError: Boolean = false,
  val showDateError: Boolean = false,
  val isNextButtonEnabled: Boolean = false
)
