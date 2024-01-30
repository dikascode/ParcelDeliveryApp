package com.dikascode.parceldeliverydemo.ui.shipment

import android.animation.ObjectAnimator
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dikascode.parceldeliverydemo.Constants.THRESHOLD_FAST_SCROLL
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
    private lateinit var shipmentViewModel: ShipmentViewModel
    private lateinit var shipmentAdapter: ShipmentAdapter
    private var lastAnimatedPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        shipmentViewModel = ViewModelProvider(this)[ShipmentViewModel::class.java]
        _binding = FragmentShipmentBinding.inflate(inflater, container, false)
        setupTabLayoutWithCounts()
        setupRecyclerView()
        observeShipments()
        binding.shipmentsRecyclerView.itemAnimator = CustomRecyclerViewItemAnimator()
        binding.shipmentsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                for (position in firstVisibleItemPosition..lastVisibleItemPosition) {
                    if (position > lastAnimatedPosition) {
                        val view = layoutManager.findViewByPosition(position) ?: continue

                        // No animation if scrolling too fast
                        if (abs(dy) > THRESHOLD_FAST_SCROLL) {
                            //do nothing
                        } else {
                            view.animate()
                                .translationY(0f)
                                .alpha(1f)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .setDuration(300)
                                .start()
                        }
                        lastAnimatedPosition = position
                    }
                }
            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateBackArrow()
        animateHeaderTitle()
        animateTabLayout()
        animateFragmentLabel()
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


    private fun animateHeaderTitle() {
        binding.headerTitle.apply {
            alpha = 0f
            translationY = 50f
            visibility = View.VISIBLE
            animate().alpha(1f).translationY(0f).setDuration(300).start()
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
        shipmentAdapter = ShipmentAdapter(mutableListOf())
        binding.shipmentsRecyclerView.adapter = shipmentAdapter
        binding.shipmentsRecyclerView.layoutManager = LinearLayoutManager(context)
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