package com.dikascode.parceldeliverydemo.ui.calculate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dikascode.parceldeliverydemo.databinding.FragmentCalculateBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}