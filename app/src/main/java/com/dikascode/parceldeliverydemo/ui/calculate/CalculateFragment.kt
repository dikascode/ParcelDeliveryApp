package com.dikascode.parceldeliverydemo.ui.calculate

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.databinding.FragmentCalculateBinding
import com.dikascode.parceldeliverydemo.utils.Utilities.animateButtonScale
import com.dikascode.parceldeliverydemo.utils.Utilities.showToast

class CalculateFragment : Fragment() {

    private var _binding: FragmentCalculateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calculateViewModel =
            ViewModelProvider(this).get(CalculateViewModel::class.java)

        _binding = FragmentCalculateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateBackArrow()
        animateLayouts()
        animateCategoryButtons()


        val categoryButtons = listOf(
            binding.buttonDocuments,
            binding.buttonGlass,
            binding.buttonLiquid,
            binding.buttonFood,
            binding.buttonElectronic,
            binding.buttonProduct,
            binding.buttonOther
        )

        categoryButtons.forEach { button ->
            button.setOnClickListener { handleButtonSelection(it as Button, categoryButtons) }
        }

        binding.calculateButton.setOnClickListener { button ->
            animateButtonScale(button)
            if (!areFieldsValid()) {
                this@CalculateFragment.context?.showToast("Please fill in all destination fields")
            } else {
                // Proceed to checkout
                findNavController().navigate(R.id.checkOutFragment)
            }
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }
    }


    private fun animateBackArrow() {
        binding.backArrow.post {
            binding.backArrow.translationX = -binding.backArrow.width.toFloat()
            binding.backArrow.visibility = View.VISIBLE
            binding.backArrow.animate()
                .translationX(0f)
                .setDuration(500)
                .start()
        }
    }
    private fun animateLayouts() {
        val viewsToAnimate = listOf(
            binding.senderLocationLinearLayout, binding.receiverLocationLinearLayout,
            binding.approxWeightLinearLayout, binding.packageOptionInputLayout,
            binding.packagingTitle, binding.categoriesTitle, binding.calculateButton, binding.destinationTitle, binding.headerTitle
        )
        viewsToAnimate.forEach { view ->
            view.post {
                view.translationY = view.height.toFloat()
                view.visibility = View.VISIBLE
                view.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(500)
                    .start()
            }
        }
    }

    private fun animateCategoryButtons() {
        val buttons = listOf(
            binding.buttonDocuments, binding.buttonGlass, binding.buttonLiquid,
            binding.buttonFood, binding.buttonElectronic, binding.buttonProduct, binding.buttonOther
        )
        var delay = 0L

        buttons.forEach { button ->
            button.post {
                button.translationX = button.width.toFloat()
                button.visibility = View.VISIBLE
                button.animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(500)
                    .setStartDelay(delay)
                    .start()
                delay += 50 // Increment delay for next button
            }
        }
    }


    private fun handleButtonSelection(selectedButton: Button, buttons: List<Button>) {
        buttons.forEach { button ->
            val isSelected = button == selectedButton
            toggleButtonAppearance(button, isSelected)
            button.isSelected = isSelected
        }
    }

    private fun toggleButtonAppearance(button: Button, isSelected: Boolean) {
        val scale = if (isSelected) 0.9f else 1f

        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            button,
            PropertyValuesHolder.ofFloat("scaleX", scale),
            PropertyValuesHolder.ofFloat("scaleY", scale)
        )
        scaleDown.duration = 300

        // Change background, text color, and icon based on selection
        button.apply {
            setBackgroundColor(ContextCompat.getColor(context, if (isSelected) R.color.black else R.color.white))
            setTextColor(ContextCompat.getColor(context, if (isSelected) R.color.white else R.color.black))

            val rightIcon = if (isSelected) ContextCompat.getDrawable(context, R.drawable.ic_check) else null
            rightIcon?.setBounds(0, 0, rightIcon.intrinsicWidth, rightIcon.intrinsicHeight)

            setCompoundDrawablesWithIntrinsicBounds(rightIcon, null, null, null)
        }

        scaleDown.start()
    }

    private fun areFieldsValid(): Boolean {
        //Delivery Info validation
        return binding.senderLocationEditText.text.isNotEmpty() &&
                binding.receiverLocationEditText.text.isNotEmpty() &&
                binding.approxWeightEditText.text.isNotEmpty()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}