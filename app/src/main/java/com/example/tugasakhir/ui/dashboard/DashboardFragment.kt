package com.example.tugasakhir.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugasakhir.DetailActivity
import com.example.tugasakhir.DetailActivity2
import com.example.tugasakhir.DetailActivity3
import com.example.tugasakhir.Detail_activity2
import com.example.tugasakhir.databinding.FragmentDashboardBinding
import com.example.tugasakhir.form_Pembayaran

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.btnMore1.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("DETAIL_TEXT", "Detail untuk Pulau Samalona")
            startActivity(intent)
        }

        binding.btnMore2.setOnClickListener {
            val intent = Intent(activity, Detail_activity2::class.java)
            intent.putExtra("DETAIL_TEXT", "Detail untuk Danau Laut Tawar")
            startActivity(intent)
        }

        binding.btnMore3.setOnClickListener {
            val intent = Intent(activity, DetailActivity2::class.java)
            intent.putExtra("DETAIL_TEXT", "Detail untuk Danau Bungi")
            startActivity(intent)
        }

        binding.btnMore4.setOnClickListener {
            val intent = Intent(activity, DetailActivity3::class.java)
            intent.putExtra("DETAIL_TEXT", "Detail untuk Tanjung Bira")
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
