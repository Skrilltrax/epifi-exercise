package dev.skrilltrax.epifiexercise.bank

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.addListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import dev.skrilltrax.epifiexercise.R
import dev.skrilltrax.epifiexercise.databinding.FragmentBankBinding
import dev.skrilltrax.epifiexercise.utils.createOpacityAnimator
import dev.skrilltrax.epifiexercise.utils.createTranslationYAnimator
import dev.skrilltrax.epifiexercise.utils.showEmptyError
import dev.skrilltrax.epifiexercise.utils.viewBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges

class BankFragment : Fragment(R.layout.fragment_bank) {
  private val binding: FragmentBankBinding by viewBinding(FragmentBankBinding::bind)
  private val viewModel: BankViewModel by viewModels()
  private var errorAnimator: AnimatorSet = AnimatorSet()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
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
      }
      .flowWithLifecycle(viewLifecycleOwner.lifecycle)
      .launchIn(viewLifecycleOwner.lifecycleScope)
  }

  private fun setupClickListeners() {
    binding.nextBtn.setOnClickListener { viewModel.nextButtonClicked() }
    binding.panUnavailableTv.setOnClickListener { viewModel.panUnavailableButtonClicked() }
  }

  private fun observeFlows() {
    viewModel.uiState
      .flowWithLifecycle(viewLifecycleOwner.lifecycle)
      .onEach(::render)
      .launchIn(viewLifecycleOwner.lifecycleScope)

    viewModel.uiEvent
      .flowWithLifecycle(viewLifecycleOwner.lifecycle)
      .onEach(::handleEvent)
      .launchIn(viewLifecycleOwner.lifecycleScope)
  }

  private fun handleEvent(event: BankUiEvent) {
    when (event) {
      BankUiEvent.EVENT_DETAILS_SUBMITTED -> {
        Toast.makeText(
            requireContext(),
            getString(R.string.details_submitted_successfully),
            Toast.LENGTH_LONG
          )
          .show()
        requireActivity().finish()
      }
      BankUiEvent.EVENT_PAN_UNAVAILABLE -> {
        requireActivity().finish()
      }
    }
  }

  private fun render(state: BankUiState) {
    with(binding) {
      if (state.isPANValid) {
        panTil.error = null
      } else {
        panTil.error = "PAN number is invalid"
      }

      if (state.isDateValid) {
        yearTil.error = null
        monthTil.error = null
        dayTil.error = null
        hideDateErrorIfVisible(dateErrorTv)
      } else {
        binding.dayTil.errorIconDrawable = null
        binding.monthTil.errorIconDrawable = null
        binding.dayTil.showEmptyError()
        binding.monthTil.showEmptyError()
        binding.yearTil.showEmptyError()
        showDateErrorIfNotVisible(dateErrorTv)
      }

      binding.nextBtn.isEnabled = state.isNextButtonEnabled
    }
  }

  private fun hideDateErrorIfVisible(dateErrorTv: TextView) {
    if (dateErrorTv.alpha != 0f) {
      errorAnimator = AnimatorSet()
      val opacityAnimation = dateErrorTv.createOpacityAnimator(false)
      errorAnimator.play(opacityAnimation)
      errorAnimator.addListener(onEnd = { dateErrorTv.alpha = 0f })
      errorAnimator.start()
    }
  }

  private fun showDateErrorIfNotVisible(dateErrorTv: TextView) {
    if (dateErrorTv.alpha != 1f) {
      errorAnimator = AnimatorSet()
      val opacityAnimation = dateErrorTv.createOpacityAnimator( true)
      val translationAnimation = dateErrorTv.createTranslationYAnimator()
      errorAnimator.playTogether(opacityAnimation, translationAnimation)
      errorAnimator.addListener(
        onEnd = {
          dateErrorTv.alpha = 1f
          dateErrorTv.translationY = 0f
        }
      )
      errorAnimator.start()
    }
  }
}
