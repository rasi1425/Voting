package com.example.votingapp.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.votingapp.R

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registrationButton = findViewById<Button>(R.id.registrationButton)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val isAdmin = isAdmin(username, password)

            if (isAdmin) {
                val intent = Intent(this, AdminActivity::class.java)
                intent.putExtra("voterUsername", username)
                startActivity(intent)
            } else {
                val isValidUser = dbHelper.validateUserCredentials(username, password)

                if (isValidUser) {
                    val intent = Intent(this, VotingActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registrationButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isAdmin(username: String, password: String): Boolean {
        return (username == "admin" && password == "123456")
    }
}
