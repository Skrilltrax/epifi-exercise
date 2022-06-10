package dev.skrilltrax.epifiexercise.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.google.android.material.textfield.TextInputLayout
import dev.skrilltrax.epifiexercise.R

fun TextInputLayout.showEmptyError() {
    this.error = " "
}

fun View.createOpacityAnimator(display: Boolean): ObjectAnimator {
    val endValue = if (display) 1f else 0f
    val opacityAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, endValue)
    opacityAnimator.duration = Constants.CAPTION_OPACITY_FADE_ANIMATION_DURATION.toLong()
    opacityAnimator.interpolator = LinearInterpolator()
    return opacityAnimator
}

fun View.createTranslationYAnimator(): ObjectAnimator {
    val captionTranslationYPx =
        context.resources.getDimensionPixelSize(R.dimen.textinput_caption_translate_y).toFloat()
    val translationYAnimator =
        ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, -captionTranslationYPx, 0f)
    translationYAnimator.duration = Constants.CAPTION_TRANSLATE_Y_ANIMATION_DURATION.toLong()
    translationYAnimator.interpolator = LinearOutSlowInInterpolator()
    return translationYAnimator
}
