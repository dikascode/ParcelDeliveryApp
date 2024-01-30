package com.dikascode.parceldeliverydemo.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast

object Utilities {
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun animateButtonScale(button: View) {
        val scaleFactor = 0.9f
        val duration = 100L

        val scaleInX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleFactor)
        val scaleInY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleFactor)
        val scaleOutX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
        val scaleOutY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)

        val scaleInAnimator = ObjectAnimator.ofPropertyValuesHolder(button, scaleInX, scaleInY).setDuration(duration)
        val scaleOutAnimator = ObjectAnimator.ofPropertyValuesHolder(button, scaleOutX, scaleOutY).setDuration(duration)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(scaleInAnimator, scaleOutAnimator)
        animatorSet.start()
    }

    fun slideUpAndFadeIn(view: View) {
        view.post {
            view.translationY = 50f
            view.alpha = 0f
            view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(500)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }
}