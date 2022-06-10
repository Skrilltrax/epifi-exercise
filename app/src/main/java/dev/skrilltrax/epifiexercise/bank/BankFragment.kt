package dev.skrilltrax.epifiexcercise.bank

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.skrilltrax.epifiexcercise.R
import dev.skrilltrax.epifiexcercise.databinding.FragmentBankBinding
import dev.skrilltrax.epifiexcercise.utils.viewBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges

class BankFragment : Fragment(R.layout.fragment_bank) {
    private val binding: FragmentBankBinding by viewBinding(FragmentBankBinding::bind)
    private val viewModel: BankViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        print("Correct Fragment")
        observeFlows()
        setupClickListeners()
        setupTextWatchers()
    }

    private fun setupTextWatchers() {
        binding.panTiet.doAfterTextChanged { editable ->
            val text = editable?.toString() ?: ""
            viewModel.validatePAN(text)
        }

        combine(
            binding.dayTiet.textChanges(),
            binding.monthTiet.textChanges(),
            binding.yearTiet.textChanges()
        ) { day, month, year ->
            viewModel.validateDate(day.toString(), month.toString(), year.toString())
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupClickListeners() {
        binding.nextBtn.setOnClickListener { viewModel.nextButtonClicked() }
        binding.panUnavailableTv.setOnClickListener { viewModel.panUnavailableButtonClicked() }
    }

    private fun observeFlows() {
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach(::render)
        viewModel.uiEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach(::handleEvent)
    }

    private fun handleEvent(event: BankUiEvent) {
        when (event) {
            BankUiEvent.EVENT_DETAILS_SUBMITTED -> {
                Toast.makeText(requireContext(), "SUBMITTED", Toast.LENGTH_LONG).show()
            }
            BankUiEvent.EVENT_PAN_UNAVAILABLE -> {
                Toast.makeText(requireContext(), "UNAVAILABLE", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun render(state: BankUiState) {
        if (state.isPANValid) {
            binding.panTil.isErrorEnabled = false
            binding.panTil.error = null
        } else {
            binding.panTil.isErrorEnabled = true
            binding.panTil.error = "PAN number is invalid"
        }

        if (state.isDateValid) {
            binding.dayTil.isErrorEnabled = false
            binding.dayTil.error = null
        } else {
            binding.dayTil.isErrorEnabled = true
            binding.dayTil.error = "Date is invalid"
        }

        binding.nextBtn.isEnabled = state.isNextButtonEnabled
    }
}
