package com.example.votingapp.UI

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.practice_project.Dtacls.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDetails.db"

        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"

        const val TABLE_VOTES = "votes"
        const val COLUMN_VOTER_USERNAME = "voter_username"
        const val COLUMN_CANDIDATE_NAME = "candidate_name"

        private const val CREATE_USER_TABLE_QUERY = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT, $COLUMN_EMAIL TEXT, $COLUMN_PHONE TEXT)"

        private const val CREATE_VOTES_TABLE_QUERY = "CREATE TABLE $TABLE_VOTES ($COLUMN_VOTER_USERNAME TEXT, $COLUMN_CANDIDATE_NAME TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USER_TABLE_QUERY)
        db?.execSQL(CREATE_VOTES_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_VOTES")
        onCreate(db)
    }

    fun registerUser(username: String, password: String, email: String, phone: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun insertVotingRecord(username: String?, candidateName: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_VOTER_USERNAME, username)
            put(COLUMN_CANDIDATE_NAME, candidateName)
        }
        return db.insert(TABLE_VOTES, null, values)
    }

    fun validateUserCredentials(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(
            TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val isValid = cursor.count > 0
      //  cursor.close()
        return isValid
    }

    fun clearUserData() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    @SuppressLint("Range")
    fun getAllUsers(): ArrayList<User> {
        val usersList = ArrayList<User>()
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT, $COLUMN_EMAIL TEXT, $COLUMN_PHONE TEXT)")
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
                )
                usersList.add(user)
            } while (cursor.moveToNext())
        }

       // cursor.close()
        return usersList
    }


    fun getVoteCountForCandidate(candidateName: String): Int {
        val db = this.readableDatabase
        val selection = "$COLUMN_CANDIDATE_NAME = ?"
        val selectionArgs = arrayOf(candidateName)
        val cursor = db.query(
            TABLE_VOTES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val voteCount = cursor.count
        cursor.close()
        return voteCount
    }




}
