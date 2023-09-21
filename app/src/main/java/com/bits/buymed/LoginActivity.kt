package com.bits.buymed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bits.buymed.interfaces.ApiService
import com.bits.buymed.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    // Define a constant for your preferences file name
    private val PREFERENCES_FILE_NAME = "MyAppPreferences"
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val baseUrl = resources.getString(R.string.base_url)
        sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

        // Add your login logic here
        val signUpTextView = findViewById<TextView>(R.id.signup_textview)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val errorTextView = findViewById<TextView>(R.id.error)

        loginButton.setOnClickListener {
//            val dbHelper = DBHelper(this)
            val email = email.text.toString()
            val password = password.text.toString()
//            val id = dbHelper.checkUser(email, password)
//            Log.d("id" ,""+id )

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl) // Replace with your base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            val loginRequest = LoginRequest(email, password)

            val call = apiService.login(loginRequest)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    val statusCode = response.code() // Get the HTTP status code
                    if (response.isSuccessful) {
                        Log.d("$$$$$ login response", "" + response.isSuccessful)
                        val editor = sharedPreferences.edit()
                        editor.putString("username", email)
                        editor.putString("password", password)
                        editor.apply()

                        val intent = Intent(this@LoginActivity, SearchMedicines::class.java)

                        // Optionally, you can pass data to the second activity using extras
                        intent.putExtra("key", "value")

                        // Start the second activity
                        startActivity(intent)
                        finish()
                    } else {
                        val errorResponseBody = response.errorBody()?.string()
                        Log.d("$$$$$ login response", "Error, Status Code: $statusCode")
                        Log.d("$$$$$ login response", "Error Body: $errorResponseBody")

                        errorTextView.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("$$$$ API Request fail", "Request failed: ${t.message}")

                }
            })

        }
    }
}

