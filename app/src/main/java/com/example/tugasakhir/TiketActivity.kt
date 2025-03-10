package com.example.tugasakhir

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TiketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)

        // Ambil data dari intent
        val nama = intent.getStringExtra("namaPemegangKartu")
        val layanan = intent.getStringExtra("layanan")
        val totalHarga = intent.getStringExtra("totalHarga")
        val nomorKartu = intent.getStringExtra("nomorKartu")
        val destinasi = intent.getStringExtra("destinasi")
        val tanggalKepergian = intent.getStringExtra("tanggalKepergian")
        val nomorTelepon = intent.getStringExtra("nomorTelepon")

        // Tampilkan data
        findViewById<TextView>(R.id.tvNamaPemegangKartu).text = "Nama: $nama"
        findViewById<TextView>(R.id.tvLayanan).text = "Layanan: $layanan"
        findViewById<TextView>(R.id.tvTotalHarga).text = "Harga: Rp $totalHarga"
        findViewById<TextView>(R.id.tvId).text = "ID: $nomorKartu"

        // Menampilkan data tambahan
        findViewById<TextView>(R.id.tvDestinasi).text = "Destinasi: $destinasi"
        findViewById<TextView>(R.id.tanggalKepergian).text = "Tanggal: $tanggalKepergian"
        findViewById<TextView>(R.id.tvNoTel).text = "Telepon: $nomorTelepon"

        // Inisialisasi tombol kembali ke menu utama
        val btnBackToMain = findViewById<Button>(R.id.btnBackToMain)
        btnBackToMain.setOnClickListener {
            // Intent untuk kembali ke menu utama (ganti MainActivity::class dengan aktivitas tujuan yang sesuai)
            val intent = Intent(this, menu::class.java)
            startActivity(intent)
            finish() // Menutup aktivitas saat ini agar tidak kembali lagi ke halaman TiketActivity
        }
    }
}
