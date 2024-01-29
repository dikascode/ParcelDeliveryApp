package com.dikascode.parceldeliverydemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dikascode.parceldeliverydemo.R
import com.dikascode.parceldeliverydemo.databinding.FragmentHomeBinding
import com.dikascode.parceldeliverydemo.model.Vehicle
import com.dikascode.parceldeliverydemo.ui.home.adapters.VehicleAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }
        return root
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}