package com.example.votingapp.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.votingapp.R

class AdminActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)


        dbHelper = DatabaseHelper(this)
//        val votingRecords = dbHelper.getAllVotingRecords()
//
//        for (record in votingRecords) {
//            Log.d("mytag", " Voter Username: ${record.voterUsername}")
//        }




        val logoutButton = findViewById<Button>(R.id.logoutButton)

        logoutButton.setOnClickListener {
            clearVoteCounts()
            navigateToLogin()
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("voting", MODE_PRIVATE)

        displayCandidateVotes()

    }

    private fun clearVoteCounts() {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_VOTES, null, null)
    }



    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayCandidateVotes() {
        val candidate1Votes = dbHelper.getVoteCountForCandidate("Candidate 1")
        val candidate2Votes = dbHelper.getVoteCountForCandidate("Candidate 2")
        val candidate3Votes = dbHelper.getVoteCountForCandidate("Candidate 3")
        val candidate4Votes = dbHelper.getVoteCountForCandidate("Candidate 4")

        findViewById<TextView>(R.id.candidate1CountTextView)?.apply {
            text = "Candidate 1 Votes: $candidate1Votes"
        }

        findViewById<TextView>(R.id.candidate2CountTextView)?.apply {
            text = "Candidate 2 Votes: $candidate2Votes"
        }

        findViewById<TextView>(R.id.candidate3CountTextView)?.apply {
            text = "Candidate 3 Votes: $candidate3Votes"
        }

        findViewById<TextView>(R.id.candidate4CountTextView)?.apply {
            text = "Candidate 4 Votes: $candidate4Votes"
        }
    }
}
