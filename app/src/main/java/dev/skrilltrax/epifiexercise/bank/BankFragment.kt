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
        Toast.makeText(requireContext(), "SUBMITTED", Toast.LENGTH_LONG).show()
      }
      BankUiEvent.EVENT_PAN_UNAVAILABLE -> {
        Toast.makeText(requireContext(), "UNAVAILABLE", Toast.LENGTH_LONG).show()
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
      val opacityAnimation = createCaptionOpacityAnimator(dateErrorTv, false)
      errorAnimator.play(opacityAnimation)
      errorAnimator.addListener(onEnd = { dateErrorTv.alpha = 0f })
      errorAnimator.start()
    }
  }

  private fun showDateErrorIfNotVisible(dateErrorTv: TextView) {
    if (dateErrorTv.alpha != 1f) {
      errorAnimator = AnimatorSet()
      val opacityAnimation = createCaptionOpacityAnimator(dateErrorTv, true)
      val translationAnimation = createCaptionTranslationYAnimator(dateErrorTv)
      errorAnimator.playTogether(opacityAnimation, translationAnimation)
      errorAnimator.addListener(onEnd = {
        dateErrorTv.alpha = 1f
        dateErrorTv.translationY = 0f
      })
      errorAnimator.start()
    }
  }

  private fun createCaptionOpacityAnimator(captionView: TextView, display: Boolean): ObjectAnimator {
    val endValue = if (display) 1f else 0f
    val opacityAnimator = ObjectAnimator.ofFloat(captionView, View.ALPHA, endValue)
    opacityAnimator.duration = CAPTION_OPACITY_FADE_ANIMATION_DURATION.toLong()
    opacityAnimator.interpolator = LinearInterpolator()
    return opacityAnimator
  }

  private fun createCaptionTranslationYAnimator(captionView: TextView): ObjectAnimator {
    val captionTranslationYPx = requireContext().resources.getDimensionPixelSize(R.dimen.textinput_caption_translate_y).toFloat()
    val translationYAnimator = ObjectAnimator.ofFloat(captionView, View.TRANSLATION_Y, -captionTranslationYPx, 0f)
    translationYAnimator.duration = CAPTION_TRANSLATE_Y_ANIMATION_DURATION.toLong()
    translationYAnimator.interpolator = LinearOutSlowInInterpolator()
    return translationYAnimator
  }

  companion object {
    /** Taken from [TextInputLayout] */

    /** Duration for the caption's vertical translation animation.  */
    private const val CAPTION_TRANSLATE_Y_ANIMATION_DURATION = 217

    /** Duration for the caption's opacity fade animation.  */
    private const val CAPTION_OPACITY_FADE_ANIMATION_DURATION = 167
  }
}
