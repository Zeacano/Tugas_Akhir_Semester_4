package com.example.tugasakhir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail4)

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
        // Koordinat Tanjung Bira
        val latitude = -5.5926
        val longitude = 120.7506
        val label = "Tanjung Bira"
        val uri = String.format("geo:%s,%s?q=%s", latitude, longitude, label)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}
