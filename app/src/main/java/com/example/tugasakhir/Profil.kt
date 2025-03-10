package com.example.tugasakhir

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Profil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profil)

        val nama = intent.getStringExtra("nama")
        val email = intent.getStringExtra("email")

        findViewById<TextView>(R.id.tvNama).text = "Nama  : $nama"
        findViewById<TextView>(R.id.tvEmail).text = "Email : $email"

        val btnBackToMain = findViewById<Button>(R.id.btnLogout)
        btnBackToMain.setOnClickListener {
            // Intent untuk kembali ke menu utama (ganti MainActivity::class dengan aktivitas tujuan yang sesuai)
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish() // Menutup aktivitas saat ini agar tidak kembali lagi ke halaman TiketActivity
        }
    }
}