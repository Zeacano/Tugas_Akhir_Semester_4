package com.example.tugasakhir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail3)

        // Menetapkan padding untuk layout utama berdasarkan inset sistem
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mendapatkan referensi ke ImageView
        val imageViewMapIcon = findViewById<ImageView>(R.id.imageViewMapIcon)

        // Menambahkan listener untuk ImageView
        imageViewMapIcon.setOnClickListener {
            // Panggil fungsi untuk membuka Google Maps
            openGoogleMaps()
        }
    }

    private fun openGoogleMaps() {
        // Koordinat Danau Pasi Bungi
        val latitude = 1.4860 // Koordinat sementara, ganti dengan koordinat yang tepat jika ada
        val longitude = 124.8489 // Koordinat sementara, ganti dengan koordinat yang tepat jika ada
        val label = "Danau Pasi Bungi"
        val uri = String.format("geo:%s,%s?q=%s", latitude, longitude, label)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}
