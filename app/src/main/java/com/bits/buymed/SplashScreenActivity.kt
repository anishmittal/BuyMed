package com.bits.buymed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
//import android.support.v4.content.ContextCompat.startActivity
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    // Delay time for SplashScreen in milliseconds
    private val SPLASH_DELAY: Long = 1000 // 3 seconds
    private val PREFERENCES_FILE_NAME = "MyAppPreferences"
    private lateinit var sharedPreferences: SharedPreferences

    // Initialize SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

        // Retrieve the username from SharedPreferences
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            // Using a Handler to delay the transition to MainActivity
            Handler().postDelayed({
                val intent = Intent(this, SearchMedicines::class.java)
                startActivity(intent)
                finish() // Close the SplashScreenActivity to prevent going back to it
            }, SPLASH_DELAY)
        } else {
            // Using a Handler to delay the transition to MainActivity
            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Close the SplashScreenActivity to prevent going back to it
            }, SPLASH_DELAY)
        }



    }
}
