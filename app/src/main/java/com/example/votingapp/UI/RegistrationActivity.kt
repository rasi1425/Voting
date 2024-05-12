package com.example.votingapp.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.votingapp.R

class RegistrationActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = DatabaseHelper(this)

        val registrationButton = findViewById<Button>(R.id.registrationButton)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val phoneEditText = findViewById<EditText>(R.id.phoneEditText)

        registrationButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()

            if (validateInput(username, password, email, phone)) {
                val newRowId = dbHelper.registerUser(username, password, email, phone)

                if (newRowId != -1L) {
                    val message = "Registration successful\nUsername: $username\nEmail: $email\nPhone: $phone"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


                    val users = dbHelper.getAllUsers()
                    for (user in users) {
                        Log.d("mytag", "ID: ${user.id}, Username: ${user.username}, Password: ${user.password}, Email: ${user.email}, Phone: ${user.phone}")
                    }
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInput(username: String, password: String, email: String, phone: String): Boolean {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
