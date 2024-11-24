package com.example.capstoneemissioncarbon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneemissioncarbon.R
import com.example.capstoneemissioncarbon.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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


        val profileImageView = binding.profileImage
        profileImageView.setImageResource(R.drawable.baseline_person_24) // Ganti dengan sumber gambar lain jika perlu

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}