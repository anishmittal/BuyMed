package com.bits.buymed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView



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
//            val dbHelper = DBHelper(this)
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val repassword = editTextRePassword.text.toString()
            Log.d("password" ,""+password )
            Log.d("repassword" ,""+repassword )
            if(password != repassword){
                error.text = "Passwords don't match!"
            }
            else {
//                val id = dbHelper.insertUser(email, password)
//                Log.d("id", "" + id)
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
            }
        }
        // Add your sign up logic here
    }
}
