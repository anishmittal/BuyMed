package com.bits.buymed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.support.v7.app.AppCompatActivity

import android.util.Log


class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Add your login logic here
        val signUpTextView = findViewById<TextView>(R.id.signup_textview)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val signupButton = findViewById<Button>(R.id.loginButton)
        val email = findViewById<EditText>(R.id.emailEditText)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val error = findViewById<TextView>(R.id.error)

        signupButton.setOnClickListener {
//            val dbHelper = DBHelper(this)
            val email = email.text.toString()
            val password = password.text.toString()
//            val id = dbHelper.checkUser(email, password)
//            Log.d("id" ,""+id )
//            if(id){
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//            else{
//                error.text = "username or password is incorrect!"
//            }

        }
    }
}
