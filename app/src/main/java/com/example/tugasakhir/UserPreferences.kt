// File: UserPreferences.kt
package com.example.tugasakhir

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserDetails(userName: String, userEmail: String) {
        with(sharedPreferences.edit()) {
            putString("USER_NAME", userName)
            putString("USER_EMAIL", userEmail)
            apply()
        }
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("USER_NAME", null)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", null)
    }
}
