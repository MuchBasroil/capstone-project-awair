package com.example.capstoneemissioncarbon.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneemissioncarbon.databinding.FragmentHistoryBinding
import com.example.capstoneemissioncarbon.ui.profile.CurrentConditions
import com.example.capstoneemissioncarbon.ui.retrofit.ApiClient

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set RecyclerView
        adapter = HistoryAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        fetchHistoryData()

        // Fetch and set data
        val historyData = getHistoryDataFromHomeFragment()
        adapter.submitList(historyData)
    }

    private fun fetchHistoryData() {
        ApiClient.apiService2.getHistoryData().enqueue(object : Callback<List<CurrentConditions>> {
            override fun onResponse(call: Call<List<CurrentConditions>>, response: Response<List<CurrentConditions>>) {
                if (response.isSuccessful && response.body() != null) {
                    if (!isAdded) return
                    val historyData = response.body()
                    // Set data ke adapter
                    adapter.submitList(historyData)
                }
            }

            override fun onFailure(call: Call<List<CurrentConditions>>, t: Throwable) {
                if (!isAdded) return
                // Tampilkan error
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getHistoryDataFromHomeFragment(): List<CurrentConditions> {
        // Simulasi data dari hari sebelumnya
        return listOf(
            CurrentConditions(
                title = "Gunakan Maskermu!",
                description = "Kualitas udara buruk di lokasi Anda. AQI mencapai 78.7",
                date = "10 Des 2024",
                status = "Healthy"
            ),
            CurrentConditions(
                title = "Kondisi Udara Bagus!",
                description = "Kualitas udara bagus di Surabaya. AQI 89.5",
                date = "9 Des 2024",
                status = "Healthy"
            ),
            CurrentConditions(
                title = "Udara Segar di Surabaya!",
                description = "Kualitas udara segar di Surabaya. AQI 84.1",
                date = "8 Des 2024",
                status = "Healthy"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
