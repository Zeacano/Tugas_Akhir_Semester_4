package com.example.tugasakhir.ui.home

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugasakhir.MainActivity2
import com.example.tugasakhir.R
import com.example.tugasakhir.databinding.FragmentHomeBinding
import com.example.tugasakhir.PhoneNumberDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var customNewSpinner: Spinner
    private var progressDialog: ProgressDialog? = null
    private var isLoading: Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val validDestinations = listOf("Samalona Island", "Danau Laut Tawar", "Danau Bungi", "Tanjung Bira")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Initialize and set Adapter for Spinner
        customNewSpinner = root.findViewById(R.id.spinner1)
        val layananList = arrayOf("Just Ticket", "Ticket and Food", "Ticket-Food-Lodge")
        customNewSpinner.adapter =
            ArrayAdapter(requireContext(), R.layout.pilihlayanan, R.id.pilihlayanantxt, layananList)

        // If there is a saved state, set spinner to that value
        if (savedInstanceState != null) {
            customNewSpinner.setSelection(savedInstanceState.getInt("spinnerSelection"))
        }

        // Initialize Button and set OnClickListener for Payment
        val buttonOpenActivity: Button = root.findViewById(R.id.btnpayf)
        buttonOpenActivity.setOnClickListener {
            // Validate input before proceeding
            if (validateInput()) {
                showLoadingDialog()
                val destinasi = binding.edtlayout1.text.toString()

                // Start Pembayaran Activity with delay to show loading
                Handler(Looper.getMainLooper()).postDelayed({
                    startPembayaranActivity(destinasi)
                }, 2000) // Show loading for 2 seconds
            }
        }

        // Initialize Button and set OnClickListener for Phone Number Verification
        val buttonVerify: Button = root.findViewById(R.id.btnverif)
        buttonVerify.setOnClickListener {
            // Request phone number permission
            PhoneNumberDialogFragment().show(childFragmentManager, "PhoneNumberDialogFragment")
        }

        // Initialize ImageButton for drawer trigger

        // Add OnClickListener to TextInputEditText for date selection
        binding.edtlayout2.setOnClickListener {
            showDatePickerDialog()
        }

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected item position in the spinner
        outState.putInt("spinnerSelection", customNewSpinner.selectedItemPosition)
    }

    override fun onResume() {
        super.onResume()
        // Dismiss the progress dialog if it is showing
        if (isLoading) {
            progressDialog?.dismiss()
            isLoading = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startPembayaranActivity(destinasi: String) {
        val intent = Intent(activity, MainActivity2::class.java).apply {
            putExtra("destinasi", destinasi)
            // Add date to intent if available
            binding.edtlayout2.text.toString().let {
                if (it.isNotBlank()) {
                    putExtra("tanggalKepergian", it)
                }
            }
            putExtra("layanan", customNewSpinner.selectedItem.toString()) // Add service data to intent
            putExtra("nomorTelepon", binding.edtlayout3.text.toString()) // Add phone number data to intent
        }
        startActivity(intent)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = sdf.format(selectedDate.time)

                binding.edtlayout2.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Set minimum date to today
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun validateInput(): Boolean {
        val destinasi = binding.edtlayout1.text.toString().trim()
        val tanggalKepergian = binding.edtlayout2.text.toString().trim()
        val layanan = customNewSpinner.selectedItem.toString()
        val nomorTelepon = binding.edtlayout3.text.toString().trim()

        // Check if any field is empty
        if (destinasi.isEmpty() || tanggalKepergian.isEmpty() || layanan.isEmpty() || nomorTelepon.isEmpty()) {
            Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (nomorTelepon.length < 10 || nomorTelepon.length > 13 || !android.text.TextUtils.isDigitsOnly(nomorTelepon)) {
            Toast.makeText(context, "Nomor telepon tidak valid", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check for generic terms in destination
        val genericTerms = listOf("danau", "laut", "tawar", "bungi", "bira")
        if (genericTerms.any { term -> destinasi.equals(term, ignoreCase = true) }) {
            Toast.makeText(context, "Nama destinasi terlalu umum, harap spesifik", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if destination is valid
        if (!isValidDestination(destinasi)) {
            Toast.makeText(context, "Destinasi tidak ada", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidDestination(destinasi: String): Boolean {
        return validDestinations.any { it.equals(destinasi, ignoreCase = true) }
    }

    private fun showLoadingDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Loading...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
        isLoading = true
    }

    fun startPembayaran1(NomorTelepon: String) {
        // Not yet implemented
    }
}
