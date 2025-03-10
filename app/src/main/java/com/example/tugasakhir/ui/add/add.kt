package com.example.tugasakhir.ui.add

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tugasakhir.databinding.FragmentAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class add : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Uploading...")
            setCancelable(false)
        }

        // Handle image selection
        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        // Handle submit button click
        binding.btnSubmitSuggestion.setOnClickListener {
            val location = binding.edtlayoutDestinationName.text.toString().trim()
            val address = binding.edtlayoutDestinationAddress.text.toString().trim()

            // Check if inputs are not empty
            if (selectedImageUri != null && location.isNotEmpty() && address.isNotEmpty()) {
                // Show ProgressDialog
                progressDialog.show()
                // Upload image to Firebase Storage
                uploadImageToStorage(selectedImageUri!!, location, address)
            } else {
                Toast.makeText(requireContext(), "Masukkan inputan dan gambar", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == android.app.Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.selectedImage.visibility = View.VISIBLE
            binding.selectedImage.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToStorage(imageUri: Uri, location: String, address: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")

        // Upload the image
        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                // Now get the download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Save this URL and other data to the Realtime Database
                    saveDataToDatabase(imageUrl, location, address)
                }
            }
            .addOnFailureListener { e ->
                // Hide ProgressDialog
                progressDialog.dismiss()
                // Handle image upload failure
                Toast.makeText(requireContext(), "Gambar gagal diupload: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDataToDatabase(imageUrl: String, location: String, address: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val suggestionRef: DatabaseReference = databaseReference.child("suggestions").push()

        // Save data to Realtime Database
        val suggestionData = HashMap<String, Any>()
        suggestionData["imageUrl"] = imageUrl
        suggestionData["alamatdestinasi"] = address
        suggestionData["namalokasi"] = location

        // Save data
        suggestionRef.setValue(suggestionData)
            .addOnSuccessListener {
                // Hide ProgressDialog
                progressDialog.dismiss()
                // Data saved successfully
                Toast.makeText(requireContext(), "Saran Berhasil Dikirim", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Hide ProgressDialog
                progressDialog.dismiss()
                // Handle database save failure
                Toast.makeText(requireContext(), "Gagal Menyimpan Saran: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
