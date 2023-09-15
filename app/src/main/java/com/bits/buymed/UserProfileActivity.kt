package com.bits.buymed

import android.content.SharedPreferences
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*


class UserProfileActivity : AppCompatActivity() {

    private val PREFERENCES_FILE_NAME = "MyAppPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.setText(sharedPreferences.getString("username", null))
    }
}
