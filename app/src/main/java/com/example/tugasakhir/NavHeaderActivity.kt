package com.example.tugasakhir
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NavHeaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header)

        // Retrieve the intent data
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userName = intent.getStringExtra("USER_NAME")

        // Find the TextViews by ID
        val headerNameTextView: TextView = findViewById(R.id.header_textView)
        val headerEmailTextView: TextView = findViewById(R.id.header_text)

        // Set the TextViews with the retrieved data
        headerNameTextView.text = userName
        headerEmailTextView.text = userEmail

        // Set an OnClickListener on the TextView
    }
}
