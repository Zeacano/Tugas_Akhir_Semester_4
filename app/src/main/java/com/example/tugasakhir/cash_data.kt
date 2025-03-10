package com.example.tugasakhir

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import kotlin.concurrent.thread

class cash_data : AppCompatActivity() {
    private lateinit var namaEditText: TextInputEditText
    private lateinit var layananSpinner: Spinner
    private lateinit var hargaTotalEditText: TextInputEditText
    private lateinit var destinasi: String
    private lateinit var tanggalKepergian: String
    private lateinit var nomorTelepon: String
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private var lastId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_data)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("TiketTunai")

        destinasi = intent.getStringExtra("destinasi") ?: ""
        tanggalKepergian = intent.getStringExtra("tanggalKepergian") ?: ""
        nomorTelepon = intent.getStringExtra("nomorTelepon") ?: ""

        namaEditText = findViewById(R.id.nama)
        layananSpinner = findViewById(R.id.spinner1)
        hargaTotalEditText = findViewById(R.id.total_semua)

        val layananList = arrayOf("Just Ticket", "Ticket and Food", "Ticket-Food-Lodge")
        layananSpinner.adapter =
            ArrayAdapter(this, R.layout.pilihlayanan, R.id.pilihlayanantxt, layananList)

        val layanan = intent.getStringExtra("layanan")
        if (layanan != null) {
            val layananIndex = layananList.indexOf(layanan)
            layananSpinner.setSelection(layananIndex)
        }

        layananSpinner.isEnabled = false // Nonaktifkan interaksi pada Spinner

        val harga = when (layanan) {
            "Just Ticket" -> "250.000"
            "Ticket and Food" -> "350.000"
            "Ticket-Food-Lodge" -> "550.000"
            else -> "0"
        }
        hargaTotalEditText.setText(harga)

        // Ambil ID terakhir dari Firebase sebelum menyimpan data baru
        getLastIdFromFirebase()

        val btnSave: Button = findViewById(R.id.btnverif)
        btnSave.setOnClickListener {
            // Validasi input sebelum pengiriman data
            if (isValidInput()) {
                showLoadingPopup() // Tampilkan popup loading sebelum pengiriman data
                Handler().postDelayed({
                    sendDataToFirebase()
                }, 2000) // Durasi 2 detik
            } else {
                // Tampilkan toast peringatan jika nama belum terisi
                Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidInput(): Boolean {
        val nama = namaEditText.text.toString().trim()
        val hargaTotal = hargaTotalEditText.text.toString().trim()

        if (nama.isEmpty()) {
            return false
        }

        if (hargaTotal.isEmpty()) {
            return false
        }

        return true
    }

    private fun getLastIdFromFirebase() {
        database.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val lastTiket = childSnapshot.child("nomorKartu").getValue(Int::class.java)
                        if (lastTiket != null) {
                            lastId = lastTiket
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani kesalahan
                Toast.makeText(this@cash_data, "Gagal mendapatkan ID terakhir", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendDataToFirebase() {
        val nama = namaEditText.text.toString().trim()
        val layanan = layananSpinner.selectedItem.toString().trim()
        val hargaTotal = hargaTotalEditText.text.toString().trim().replace(".", "").toInt()

        // Increment ID
        lastId++

        val tiketData = mapOf(
            "namaPemegangKartu" to nama,
            "nomorKartu" to lastId,
            "totalHarga" to hargaTotal,
            "layanan" to layanan,
            "destinasi" to destinasi,
            "tanggalKepergian" to tanggalKepergian,
            "nomorTelepon" to nomorTelepon
        )

        // Tulis data ke Firebase di node "TiketTunai"
        val uniqueId = database.push().key ?: "unique_id_$lastId"
        database.child(uniqueId).setValue(tiketData).addOnCompleteListener { task ->
            dismissLoadingPopup() // Sembunyikan popup loading setelah pengiriman data selesai

            if (task.isSuccessful) {
                showConfirmationDialog()
            } else {
                // Tangani kesalahan
                task.exception?.printStackTrace()
                Toast.makeText(this, "Gagal mengirim data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoadingPopup() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun dismissLoadingPopup() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Inputan telah di konfirmasi")
        builder.setMessage("Silakan datang dan bayar di tempat keberangkatan. Terima kasih.")
        builder.setPositiveButton("OK") { dialog, _ ->
            val intent = Intent(this, TiketActivity::class.java).apply {
                putExtra("nomorKartu", lastId.toString()) // Menambahkan data ID ke intent
                putExtra("namaPemegangKartu", namaEditText.text.toString().trim())
                putExtra("layanan", layananSpinner.selectedItem.toString().trim())
                putExtra("totalHarga", hargaTotalEditText.text.toString().trim())
                putExtra("destinasi", destinasi)
                putExtra("tanggalKepergian", tanggalKepergian)
                putExtra("nomorTelepon", nomorTelepon) // Tambahkan nomor telepon ke intent
            }
            startActivity(intent)
            dialog.dismiss()
        }
        builder.show()
    }
}
