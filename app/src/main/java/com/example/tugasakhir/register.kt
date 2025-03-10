package com.example.tugasakhir

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class register : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Menampilkan pop-up "Silahkan buat akun anda"
        showCreateAccountPopup()

        val editTextNama = findViewById<EditText>(R.id.editTextNama)
        val editTextEmail = findViewById<EditText>(R.id.editTextText2)
        val editTextPassword = findViewById<EditText>(R.id.editTextText3)

        // Nonaktifkan autofill untuk inputan nama
        editTextNama.setImportantForAutofill(EditText.IMPORTANT_FOR_AUTOFILL_NO)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            val nama = editTextNama.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Validasi input
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi format email menggunakan Patterns.EMAIL_ADDRESS
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Masukkan email yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tampilkan pop-up loading
            showLoadingPopup()

            // Periksa apakah email sudah ada di Firebase Realtime Database
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Registrasi")

            ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Sembunyikan pop-up loading
                        dismissLoadingPopup()

                        // Jika email sudah ada, tampilkan pesan kesalahan
                        Toast.makeText(this@register, "Email sudah terdaftar, gunakan email lain.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Simpan data ke Firebase Realtime Database
                        val userId = ref.push().key
                        if (userId != null) {
                            val user = User(nama, email, password)
                            ref.child(userId).setValue(user).addOnCompleteListener { task ->
                                // Sembunyikan pop-up loading
                                dismissLoadingPopup()

                                if (task.isSuccessful) {
                                    // Menampilkan pop-up "Akun Anda Telah Dibuat"
                                    showAccountCreatedPopup()

                                    // Tunda pemindahan ke halaman login
                                    Handler().postDelayed({
                                        // Arahkan ke aktivitas login setelah membuat akun
                                        val intent = Intent(this@register, login::class.java)
                                        startActivity(intent)
                                    }, 2000)
                                } else {
                                    // Jika penyimpanan gagal, tampilkan pesan kesalahan
                                    Toast.makeText(this@register, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Sembunyikan pop-up loading
                    dismissLoadingPopup()

                    // Tangani kemungkinan kesalahan.
                    Toast.makeText(this@register, "Kesalahan database: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showCreateAccountPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ini adalah halaman registrasi!!")
        builder.setMessage("Selamat datang dan silahkan buat akun Anda terlebih dahulu.")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showAccountCreatedPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registrasi berhasil!!")
        builder.setMessage("Akun Anda telah berhasil dibuat. Silahkan login.")
        val dialog = builder.create()
        dialog.show()
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

    data class User(val nama: String, val email: String, val password: String)
}
