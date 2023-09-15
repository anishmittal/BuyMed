package com.bits.buymed


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.bits.buymed.interfaces.ApiService
import com.bits.buymed.model.SignUpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signupButton = findViewById<Button>(R.id.button_sign_up)
        val editTextEmail = findViewById<EditText>(R.id.edit_text_email)
        val editTextPassword = findViewById<EditText>(R.id.edit_text_password)
        val editTextRePassword = findViewById<EditText>(R.id.edit_text_reenterpassword)
        val error = findViewById<TextView>(R.id.error)

        signupButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val repassword = editTextRePassword.text.toString()
            Log.d("$$ password" ,""+password )
            Log.d("$$ repassword" ,""+repassword )
            if(password != repassword){
                error.text = "Passwords don't match!"
            }
            else {
//http://192.168.0.184:8000/
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000") // Replace with your base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)

                val signUpRequest = SignUpRequest(email, password, repassword)

                val call = apiService.signUp(signUpRequest)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        val statusCode = response.code() // Get the HTTP status code
                        val errorResponseBody = response.errorBody()?.string()
                        if (response.isSuccessful) {
                            Log.d("$$$$$ response" ,""+response.isSuccessful )
                            val intent = Intent(this@SignUpActivity, SearchMedicines::class.java)

                            // Optionally, you can pass data to the second activity using extras
                            intent.putExtra("key", "value")

                            // Start the second activity
                            startActivity(intent)
                            finish()
                        } else {
                            Log.d("$$$$$ response", "Error, Status Code: $statusCode")
                            Log.d("$$$$$ response", "Error Body: $errorResponseBody")
                            if (errorResponseBody != null && errorResponseBody.contains("8 characters")) {
                                error.text = "password should be 8 characters long"
                            }
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("$$$$ API Request fail", "Request failed: ${t.message}")

                    }
                })

            }
        }
    }
}
