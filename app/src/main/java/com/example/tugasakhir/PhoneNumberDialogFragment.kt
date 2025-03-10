package com.example.tugasakhir

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.tugasakhir.ui.home.HomeFragment

class PhoneNumberDialogFragment : DialogFragment() {

    private val SMS_PERMISSION_REQUEST_CODE = 101
    private lateinit var editTextPhoneNumber: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Inflate layout for dialog
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_phone_number_dialog_fragment, null)
            editTextPhoneNumber = view.findViewById<EditText>(R.id.editTextPhoneNumber)

            // Set dialog title and buttons
            builder.setTitle("Masukkan Nomor Telepon")
                .setView(view)
                .setPositiveButton("Submit") { _, _ ->
                    // Check SMS permission
                    if (checkPermission()) {
                        // Permission granted, send SMS
                        sendSMS()
                    } else {
                        // Request permission
                        requestPermissions(
                            arrayOf(Manifest.permission.SEND_SMS),
                            SMS_PERMISSION_REQUEST_CODE
                        )
                    }
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create().apply {
                // Automatically show keyboard when dialog appears
                window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun sendSMS() {
        val phoneNumber = editTextPhoneNumber.text.toString()
        val message = "Jika anda menerima pesan ini maka anda telah terverifikasi"

        try {
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null)
            showSuccessDialog()
            (activity as? HomeFragment)?.startPembayaran1(phoneNumber)
        } catch (e: Exception) {
            showErrorDialog()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pesan anda telah terkirim.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Gagal mengirim pesan.")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
                sendSMS()
            } else {
                // Permission denied
                // Handle permission denied case
            }
        }
    }

    override fun onResume() {
        super.onResume()
        editTextPhoneNumber.requestFocus()
    }
}
