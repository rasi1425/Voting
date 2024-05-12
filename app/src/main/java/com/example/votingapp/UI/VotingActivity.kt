package com.example.votingapp.UI

import android.content.Context

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.votingapp.R

class VotingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        sharedPreferences = getSharedPreferences("voting", Context.MODE_PRIVATE)
        dbHelper = DatabaseHelper(this)

        val candidateRadioGroup = findViewById<RadioGroup>(R.id.candidateRadioGroup)
        val voteButton = findViewById<Button>(R.id.voteButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            clearSession()
        }
        val username = intent.getStringExtra("username")

        voteButton.setOnClickListener {
            if (sharedPreferences.getBoolean("${username}_hasVoted", false)) {
                showMessage("You have already voted.")
            } else {
                val selectedRadioButtonId = candidateRadioGroup.checkedRadioButtonId
                if (selectedRadioButtonId != -1) {
                    val selectedCandidate = findViewById<RadioButton>(selectedRadioButtonId)
                    val candidateName = selectedCandidate.text.toString()
                    sharedPreferences.edit().putBoolean("${username}_hasVoted", true).apply()

                    saveVoteDetails(username, candidateName)

                    showMessage("Thank you for voting!")
                } else {
                    showMessage("Please select a candidate.")
                }
            }
        }
    }

    private fun clearSession() {
        dbHelper.clearUserData()

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    private fun saveVoteDetails(username: String?, candidateName: String) {
        val newRow = dbHelper.insertVotingRecord(username, candidateName)

        if (newRow != -1L) {
            val message = "Vote successfully recorded for\nVoter: $username\nCandidate: $candidateName"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to record vote.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
