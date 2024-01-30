package com.dikascode.parceldeliverydemo.ui.home

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.databinding.FragmentHomeBinding
import com.dikascode.parceldeliverydemo.model.Vehicle
import com.dikascode.parceldeliverydemo.ui.home.adapters.SearchResultAdapter
import com.dikascode.parceldeliverydemo.ui.home.adapters.VehicleAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        val searchResultAdapter = SearchResultAdapter(mutableListOf())

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.shipmentItems.observe(viewLifecycleOwner) { shipmentItems ->
            searchResultAdapter.updateData(shipmentItems)
        }
        binding.shipmentsRecyclerView.adapter = searchResultAdapter


        setupSearchEditText()
        setupBackArrow()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data
        val vehicleList = listOf(
            Vehicle("Ocean freight", "International", R.drawable.freight_image),
            Vehicle("Cargo freight", "Reliable", R.drawable.cargo_van),
            Vehicle("Air freight", "International", R.drawable.airplane)
        )

        val adapter = VehicleAdapter(vehicleList)
        binding.vehiclesRecyclerView.adapter = adapter
        binding.vehiclesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)



        animateTopAndLowerSections()
    }

    private fun animateTopAndLowerSections() {
        binding.upperSection.visibility = View.INVISIBLE
        binding.lowerSection.visibility = View.INVISIBLE

        // Transitions postponed until views have been created and measured
        view?.post {
            val slideFromTop = Slide(Gravity.TOP).apply {
                addTarget(binding.upperSection)
                duration = 300 // Duration in milliseconds
            }

            val lowerSectionTransitionSet = TransitionSet().apply {
                // Slide in from the bottom
                addTransition(Slide(Gravity.BOTTOM).apply {
                    addTarget(binding.lowerSection)
                    duration = 300 // Duration in milliseconds
                })
                // Fade in
                addTransition(Fade().apply {
                    addTarget(binding.lowerSection)
                    startDelay = 200 // To start after sliding up
                    duration = 400 // Duration in milliseconds
                })
            }


            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, slideFromTop)
            binding.upperSection.visibility = View.VISIBLE

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, lowerSectionTransitionSet)
            binding.lowerSection.visibility = View.VISIBLE
        }

//       RecyclerView animation
        val context = binding.root.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right)
        binding.vehiclesRecyclerView.layoutAnimation = controller
        binding.vehiclesRecyclerView.adapter?.notifyDataSetChanged()
        binding.vehiclesRecyclerView.scheduleLayoutAnimation()
    }

    private fun setupSearchEditText() {
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                animateBackArrowIn()
                animateHeaderOut()
                binding.backArrow.visibility = View.VISIBLE
                binding.searchResultLayout.visibility = View.VISIBLE
                binding.nonSearchLayout.visibility = View.GONE
            }
        }

        binding.searchEditText.addTextChangedListener { text ->
            val filteredList = homeViewModel.shipmentItems.value?.filter {
                it.trackingNumber.contains(text.toString(), ignoreCase = true)
            } ?: listOf()
            (binding.shipmentsRecyclerView.adapter as SearchResultAdapter).updateData(filteredList)
        }

    }

    private fun setupBackArrow() {
        binding.backArrow.setOnClickListener {
            binding.searchEditText.clearFocus()
            hideKeyboard()

            //A little delay to allow animation in onBindViewHolder load
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.navigation_home)
            }, 100)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        view?.let {
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun animateBackArrowIn() {
        binding.backArrow.post {
            val backArrow = binding.backArrow
            backArrow.translationX = -backArrow.width.toFloat() // Start position off-screen to the left
            backArrow.visibility = View.VISIBLE

            val animator = ObjectAnimator.ofFloat(backArrow, "translationX", 0f).apply {
                duration = 300
            }
            animator.start()
        }
    }


    private fun animateHeaderOut() {
        val headerSection = binding.headerSection

        val transition = Slide(Gravity.TOP).apply {
            addTarget(headerSection)
            duration = 600 // Duration in milliseconds
            interpolator = AccelerateInterpolator()
            addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    headerSection.visibility = View.GONE
                }
            })
        }

        TransitionManager.beginDelayedTransition(binding.upperSection as ViewGroup, transition)
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}