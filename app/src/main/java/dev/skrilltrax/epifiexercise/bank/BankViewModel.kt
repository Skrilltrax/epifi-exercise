package dev.skrilltrax.epifiexercise.bank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.skrilltrax.epifiexercise.validation.DateValidator
import dev.skrilltrax.epifiexercise.validation.PANValidator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BankViewModel : ViewModel() {
  private val panValidator = PANValidator()
  private val dateValidator = DateValidator()

  private val _uiState = MutableStateFlow(BankUiState())
  val uiState = _uiState.asStateFlow()

  private val _uiEvent = MutableSharedFlow<BankUiEvent>()
  val uiEvent = _uiEvent.asSharedFlow()

  fun validatePAN(text: String) {
    // If PAN is empty, do not show an error. Also, disable next button.
    if (text.isEmpty() || text.length != PANValidator.PAN_LENGTH) {
      _uiState.update { uiState -> uiState.copy(isPANValid = false, showPANError = false, isNextButtonEnabled = false) }
      return
    }

    val isValid = panValidator.isValid(text)
    _uiState.update { uiState ->
      val enableNextButton = isValid && uiState.isDateValid
      uiState.copy(isPANValid = isValid, showPANError = !isValid, isNextButtonEnabled = enableNextButton)
    }
  }

  fun validateDate(day: String, month: String, year: String) {
    // If day field of date is empty, do not show an error. Also, disable next button.
    if (day.isEmpty() || day.length < DateValidator.DAY_LENGTH) {
      _uiState.update { uiState -> uiState.copy(isDateValid = false, showDateError = false, isNextButtonEnabled = false) }
      return
    }

    // If month field of date is empty, do not show an error. Also, disable next button.
    if (month.isEmpty() || month.length < DateValidator.MONTH_LENGTH) {
      _uiState.update { uiState -> uiState.copy(isDateValid = false, showDateError = false, isNextButtonEnabled = false) }
      return
    }

    // If year field of date is empty, do not show an error. Also, disable next button.
    if (year.isEmpty() || year.length < DateValidator.YEAR_LENGTH) {
      _uiState.update { uiState -> uiState.copy(isDateValid = false, showDateError = false, isNextButtonEnabled = false) }
      return
    }

    val date = "$day/$month/$year"
    val isValid = dateValidator.isValid(date)

    _uiState.update { uiState ->
      val enableNextButton = isValid && uiState.isPANValid
      uiState.copy(isDateValid = isValid, showDateError = !isValid, isNextButtonEnabled = enableNextButton)
    }
  }

  fun nextButtonClicked() {
    viewModelScope.launch { _uiEvent.emit(BankUiEvent.EVENT_DETAILS_SUBMITTED) }
  }

  fun panUnavailableButtonClicked() {
    viewModelScope.launch { _uiEvent.emit(BankUiEvent.EVENT_PAN_UNAVAILABLE) }
  }
}
