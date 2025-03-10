package com.example.tugasakhir

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val destinasi = intent.getStringExtra("destinasi")
        val tanggalKepergian = intent.getStringExtra("tanggalKepergian")
        val layanan = intent.getStringExtra("layanan") // Mendapatkan data layanan
        val nomorTelepon = intent.getStringExtra("nomorTelepon") // Mendapatkan data nomor telepon

        // ImageView untuk mengantarkan data ke form_Pembayaran
        val ivArrow: ImageView = findViewById(R.id.iv_arrow_bni)
        ivArrow.setOnClickListener {
            val intent = Intent(this, form_Pembayaran::class.java).apply {
                putExtra("destinasi", destinasi)
                putExtra("tanggalKepergian", tanggalKepergian)
                putExtra("layanan", layanan) // Menambahkan data layanan ke intent
                putExtra("nomorTelepon", nomorTelepon) // Menambahkan data nomor telepon ke intent
            }
            startActivity(intent)
        }

        // Tambahkan listener untuk ImageView lain jika diperlukan
        val ivArrow1: ImageView = findViewById(R.id.iv_arrow_bri)
        ivArrow1.setOnClickListener {
            val intent = Intent(this, form_Pembayaran::class.java).apply {
                putExtra("destinasi", destinasi)
                putExtra("tanggalKepergian", tanggalKepergian)
                putExtra("layanan", layanan) // Menambahkan data layanan ke intent
                putExtra("nomorTelepon", nomorTelepon) // Menambahkan data nomor telepon ke intent
            }
            startActivity(intent)
        }

        val ivArrow2: ImageView = findViewById(R.id.iv_arrow_mandiri)
        ivArrow2.setOnClickListener {
            val intent = Intent(this, form_Pembayaran::class.java).apply {
                putExtra("destinasi", destinasi)
                putExtra("tanggalKepergian", tanggalKepergian)
                putExtra("layanan", layanan) // Menambahkan data layanan ke intent
                putExtra("nomorTelepon", nomorTelepon) // Menambahkan data nomor telepon ke intent
            }
            startActivity(intent)
        }

        val ivArrow3: ImageView = findViewById(R.id.iv_arrow_cash)
        ivArrow3.setOnClickListener {
            val intent = Intent(this, cash_data::class.java).apply {
                putExtra("destinasi", destinasi)
                putExtra("tanggalKepergian", tanggalKepergian)
                putExtra("layanan", layanan) // Menambahkan data layanan ke intent
                putExtra("nomorTelepon", nomorTelepon) // Menambahkan data nomor telepon ke intent
            }
            startActivity(intent)
        }

    }
}
