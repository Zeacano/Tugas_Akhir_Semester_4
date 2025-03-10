package com.example.tugasakhir

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class form_Pembayaran : AppCompatActivity() {
    private lateinit var customNewSpinner: Spinner
    private lateinit var totalTextView: TextInputEditText
    private lateinit var namaPemegangKartu: TextInputEditText
    private lateinit var nomorKartu: TextInputEditText
    private lateinit var tanggalBerlaku: TextInputEditText
    private lateinit var cvv: TextInputEditText
    private lateinit var destinasi: String
    private lateinit var tanggalKepergian: String
    private lateinit var nomorTelepon: String
    private lateinit var layanan: String
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pembayaran)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Tiket")

        // Ambil data destinasi, tanggalKepergian, dan nomorTelepon dari intent
        destinasi = intent.getStringExtra("destinasi") ?: ""
        tanggalKepergian = intent.getStringExtra("tanggalKepergian") ?: ""
        nomorTelepon = intent.getStringExtra("nomorTelepon") ?: ""
        layanan = intent.getStringExtra("layanan") ?: ""

        // Inisialisasi view
        customNewSpinner = findViewById(R.id.spinner1)
        totalTextView = findViewById(R.id.total_semua)
        namaPemegangKartu = findViewById(R.id.edtCardholderName)
        nomorKartu = findViewById(R.id.edtCardNumber)
        tanggalBerlaku = findViewById(R.id.edtExpirationDate)
        cvv = findViewById(R.id.edtCvvCode)

        val pilihanLayananList = arrayOf("Just Ticket", "Ticket and Food", "Ticket-Food-Lodge")
        customNewSpinner.adapter =
            ArrayAdapter(this, R.layout.pilihlayanan, R.id.pilihlayanantxt, pilihanLayananList)

        val layananIndex = pilihanLayananList.indexOf(layanan)
        customNewSpinner.setSelection(layananIndex)
        customNewSpinner.isEnabled = false

        val harga = when (layananIndex) {
            0 -> "250000"
            1 -> "350000"
            2 -> "550000"
            else -> "0"
        }
        totalTextView.setText(harga)

        val btnSave: Button = findViewById(R.id.btnverif)
        btnSave.setOnClickListener {
            // Pastikan input valid sebelum mengirim data
            if (isValidInput()) {
                sendDataWithIntent()
            }
        }
    }

    private fun sendDataWithIntent() {
        val nama = namaPemegangKartu.text.toString().trim()
        val nomor = nomorKartu.text.toString().trim().substring(0, 5) // Ambil 5 digit pertama dari nomor kartu
        val berlaku = tanggalBerlaku.text.toString().trim()
        val cvvCode = cvv.text.toString().trim()
        val totalHarga = totalTextView.text.toString().trim()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Memproses Pembayaran...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val tiketData = mapOf(
            "namaPemegangKartu" to nama,
            "nomorKartu" to nomor,
            "masaBerlaku" to berlaku,
            "Password" to cvvCode,
            "totalHarga" to totalHarga,
            "layanan" to layanan,
            "destinasi" to destinasi,
            "tanggalKepergian" to tanggalKepergian,
            "nomorTelepon" to nomorTelepon
        )

        // Tulis data ke Firebase
        database.push().setValue(tiketData).addOnCompleteListener { task ->
            progressDialog.dismiss()

            if (task.isSuccessful) {
                showPaymentSuccessDialog()

                Handler().postDelayed({
                    val intent = Intent(this, TiketActivity::class.java)
                    intent.putExtra("namaPemegangKartu", nama)
                    intent.putExtra("nomorKartu", nomor)
                    intent.putExtra("masaBerlaku", berlaku)
                    intent.putExtra("cvv", cvvCode)
                    intent.putExtra("totalHarga", totalHarga)
                    intent.putExtra("layanan", layanan)
                    intent.putExtra("destinasi", destinasi)
                    intent.putExtra("tanggalKepergian", tanggalKepergian)
                    intent.putExtra("nomorTelepon", nomorTelepon)

                    startActivity(intent)
                }, 2000)
            } else {
                // Handle the error
                task.exception?.printStackTrace()
                Toast.makeText(this, "Gagal mengirim data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidInput(): Boolean {
        val nama = namaPemegangKartu.text.toString().trim()
        val nomor = nomorKartu.text.toString().trim()
        val berlaku = tanggalBerlaku.text.toString().trim()
        val cvvCode = cvv.text.toString().trim()

        if (nama.isEmpty() || nomor.isEmpty() || berlaku.isEmpty() || cvvCode.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun showPaymentSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_success_payment)
        dialog.setCancelable(false)

        val btnOk: Button = dialog.findViewById(R.id.btnOk)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
