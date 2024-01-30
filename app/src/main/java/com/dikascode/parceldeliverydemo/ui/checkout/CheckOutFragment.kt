package com.dikascode.parceldeliverydemo.ui.checkout

import android.animation.ValueAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.databinding.FragmentCalculateBinding
import com.dikascode.parceldeliverydemo.databinding.FragmentCheckOutBinding
import com.dikascode.parceldeliverydemo.utils.Constants.CHECK_OUT_AMOUNT
import com.dikascode.parceldeliverydemo.utils.Utilities
import com.dikascode.parceldeliverydemo.utils.Utilities.slideUpAndFadeIn

class CheckOutFragment : Fragment() {
    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CheckOutFragment()
    }

    private lateinit var viewModel: CheckOutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Slide up and fade in animations
        slideUpAndFadeIn(binding.estimatedAmountTitleTextView)
        slideUpAndFadeIn(binding.logoImageView)
        slideUpAndFadeIn(binding.backToHomeButton)

        // Shrink animation for packageImageView
        growAnimation(binding.packageImageView)

        // Count up animation for estimatedAmountTextView
        countUpAnimation(binding.estimatedAmountTextView, CHECK_OUT_AMOUNT)

        binding.backToHomeButton.setOnClickListener { button ->
            Utilities.animateButtonScale(button)
            findNavController().navigate(R.id.navigation_home)
        }
    }

    private fun growAnimation(view: ImageView) {
        view.post {
            // Starting from a smaller scale
            view.scaleX = 0.0f
            view.scaleY = 0.0f

            view.animate()
                .scaleX(1f) // Original scale
                .scaleY(1f) // Original scale
                .setDuration(500)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }


    private fun countUpAnimation(textView: TextView, finalAmount: Int) {
        ValueAnimator.ofInt(0, finalAmount).apply {
            duration = 1000
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                textView.text = "$$value USD"
            }
            start()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[CheckOutViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}