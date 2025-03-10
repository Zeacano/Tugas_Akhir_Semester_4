package com.example.tugasakhir

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class login : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val inputEmail = findViewById<EditText>(R.id.editTextText2).text.toString()
            val inputPassword = findViewById<EditText>(R.id.editTextText3).text.toString()

            // Tampilkan pop-up loading
            showLoadingPopup()

            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Registrasi")

            ref.orderByChild("email").equalTo(inputEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Sembunyikan pop-up loading
                    dismissLoadingPopup()

                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            if (user?.password == inputPassword) {
                                // Menampilkan pesan selamat datang menggunakan AlertDialog
                                val builder = AlertDialog.Builder(this@login)
                                builder.setTitle("Selamat Datang, ${user.nama}")
                                builder.setMessage("Silahkan ke menu utama!!")
                                builder.setPositiveButton("OK") { dialog, which ->
                                    // Arahkan ke aktivitas utama dan kirim data email dan nama
                                    val intent = Intent(this@login, menu::class.java).apply {
                                        putExtra("USER_EMAIL", user.email)
                                        putExtra("USER_NAME", user.nama)
                                    }
                                    startActivity(intent)
                                }
                                val dialog = builder.create()
                                dialog.show()
                                return
                            }
                        }
                        // Jika password tidak cocok
                        Toast.makeText(this@login, "Password salah", Toast.LENGTH_SHORT).show()
                    } else {
                        // Jika email tidak ditemukan
                        Toast.makeText(this@login, "Akun Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Sembunyikan pop-up loading
                    dismissLoadingPopup()

                    // Tangani kemungkinan kesalahan.
                    Toast.makeText(this@login, "Kesalahan database: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
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

    data class User(val nama: String = "", val email: String = "", val password: String = "")
}
