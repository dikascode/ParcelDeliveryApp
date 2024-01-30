package com.dikascode.parceldeliverydemo.ui.shipment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.utils.Constants.THRESHOLD_FAST_SCROLL
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.databinding.FragmentShipmentBinding
import com.dikascode.parceldeliverydemo.ui.shipment.adapter.ShipmentAdapter
import com.dikascode.parceldeliverydemo.ui.shipment.custom_animation.CustomRecyclerViewItemAnimator
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import kotlin.math.abs

class ShipmentFragment : Fragment() {

    private var _binding: FragmentShipmentBinding? = null
    private val binding get() = _binding!!
    private val shipmentViewModel by lazy { ViewModelProvider(this)[ShipmentViewModel::class.java] }
    private val shipmentAdapter by lazy { ShipmentAdapter(mutableListOf()) }

    companion object {
        private const val ANIMATION_DURATION = 300L
        private const val FAST_SCROLL_THRESHOLD = 50
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipmentBinding.inflate(inflater, container, false)
        initializeUI()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateBackArrow()
        animateHeaderTitle()
        animateTabLayout()
        animateFragmentLabel()

        binding.backArrow.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }
    }

    private fun initializeUI() {
        setupTabLayoutWithCounts()
        setupRecyclerView()
        observeShipments()
        setupScrollListener()
        setupClickListeners()
    }

    private fun setupScrollListener() {
        binding.shipmentsRecyclerView.addOnScrollListener(ShipmentScrollListener())
    }

    private fun setupClickListeners() {
        binding.backArrow.setOnClickListener { navigateToHome() }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.navigation_home)
    }




    private fun setupTabLayoutWithCounts() {
        val tabTitles = arrayOf("All", "Completed", "In progress", "Pending order", "Cancelled")
        val tabCounts = arrayOf(12, 5, 3, 4, 0)

        tabTitles.zip(tabCounts).forEachIndexed { index, (title, count) ->
            val tab = binding.tabs.newTab()
            tab.text = title
            binding.tabs.addTab(tab)

            // Add badges to tabs
            val badge = binding.tabs.getTabAt(index)?.orCreateBadge
            badge?.number = count
            badge?.isVisible = count > 0

            // Set initial badge colors
            setBadgeColor(badge, index == 0)
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val status = when (tab?.text.toString()) {
                    "All" -> ""
                    "Completed" -> "loading"
                    "In progress" -> "in-progress"
                    "Pending order" -> "pending"
                    "Cancelled" -> "cancelled"
                    else -> ""
                }
                shipmentViewModel.filterShipments(status)
                setBadgeColor(tab?.badge, true)

                //Animate
                tab?.let {
                    animateTabTitle(it, 0.9f, 300)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setBadgeColor(tab?.badge, false)
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


    private fun setBadgeColor(badge: BadgeDrawable?, isSelected: Boolean) {
        badge?.backgroundColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.orange)
        } else {
            ContextCompat.getColor(requireContext(), R.color.light_purple)
        }

        badge?.badgeTextColor = if (isSelected) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.light_gray_background)
        }
    }

    private fun animateTabTitle(tab: TabLayout.Tab, scale: Float, duration: Long) {
        val textView = (tab.view as ViewGroup).getChildAt(1) as TextView
        ObjectAnimator.ofPropertyValuesHolder(
            textView,
            PropertyValuesHolder.ofFloat("scaleX", scale),
            PropertyValuesHolder.ofFloat("scaleY", scale)
        ).apply {
            this.duration = duration
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
            start()
        }
    }



    private fun animateBackArrow() {
        binding.backArrow.post {
            binding.backArrow.apply {
                translationX = -width.toFloat()
                visibility = View.VISIBLE
                ObjectAnimator.ofFloat(this, "translationX", 0f).apply {
                    duration = 300
                    start()
                }
            }
        }
    }

    inner class ShipmentScrollListener : RecyclerView.OnScrollListener() {
        private var lastAnimatedPosition = -1

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (abs(dy) > FAST_SCROLL_THRESHOLD) return

            (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                animateVisibleItems(layoutManager, firstVisibleItemPosition)
            }
        }

        private fun animateVisibleItems(layoutManager: LinearLayoutManager, firstVisibleItemPosition: Int) {
            val visibleItemCount = layoutManager.childCount
            val endPosition = firstVisibleItemPosition + visibleItemCount
            for (position in firstVisibleItemPosition until endPosition) {
                if (position > lastAnimatedPosition) {
                    layoutManager.findViewByPosition(position)?.animateItem()
                    lastAnimatedPosition = position
                }
            }
        }

        private fun View.animateItem() {
            this.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(ANIMATION_DURATION)
                .start()
        }
    }

    private fun animateHeaderTitle() {
        binding.headerTitle.apply {
            alpha = 0f
            translationY = 50f
            visibility = View.VISIBLE
            animate().alpha(1f).translationY(0f).setDuration(500).start()
        }
    }

    private fun animateTabLayout() {
        for (i in 0 until binding.tabs.tabCount) {
            val tab = binding.tabs.getTabAt(i)
            val tabView = (tab?.view as? ViewGroup)?.getChildAt(1) as? TextView

            tabView?.post {
                tabView.apply {
                    alpha = 0f
                    translationX = width.toFloat()

                    animate()
                        .alpha(1f)
                        .translationX(0f)
                        .setDuration(500)
                        .setInterpolator(DecelerateInterpolator())
                        .start()
                }
            }
        }
    }



    private fun animateFragmentLabel() {
        binding.fragmentLabel.apply {
            translationY = 50f
            visibility = View.VISIBLE
            ObjectAnimator.ofFloat(this, "translationY", 0f).apply {
                duration = 300
                start()
            }
        }
    }


    private fun setupRecyclerView() {
        binding.shipmentsRecyclerView.adapter = shipmentAdapter
    }

    private fun observeShipments() {
        shipmentViewModel.shipments.observe(viewLifecycleOwner) { shipments ->
            shipmentAdapter.updateData(shipments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}