package com.dikascode.parceldeliverydemo.ui.shipment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.databinding.FragmentShipmentBinding
import com.dikascode.parceldeliverydemo.ui.shipment.adapter.ShipmentAdapter
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout

class ShipmentFragment : Fragment() {

    private var _binding: FragmentShipmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var shipmentViewModel: ShipmentViewModel
    private lateinit var shipmentAdapter: ShipmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        shipmentViewModel = ViewModelProvider(this).get(ShipmentViewModel::class.java)
        _binding = FragmentShipmentBinding.inflate(inflater, container, false)
        setupTabLayoutWithCounts()
        setupRecyclerView()
        observeShipments()
        return binding.root
    }

    private fun setupTabLayoutWithCounts() {
        val tabTitles = arrayOf("All", "Completed", "In progress", "Pending order", "Cancelled")
        val tabCounts = arrayOf(12, 5, 3, 4, 0)

        tabTitles.forEach { title ->
            val tab = binding.tabs.newTab()
            tab.text = title
            binding.tabs.addTab(tab)
        }

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