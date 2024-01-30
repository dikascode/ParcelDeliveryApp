package com.dikascode.parceldeliverydemo.ui.shipment.custom_animation

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerViewItemAnimator: DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.apply {
            alpha = 0f
            translationY = height.toFloat()

            animate()
                .alpha(1f)
                .translationY(0f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(300)
                .setListener(null)
        }
        return true
    }
}