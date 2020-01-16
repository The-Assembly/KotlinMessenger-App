package com.skapps12345

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        button_login.setOnClickListener {

            val email = email_login.text.toString()
            val password = password_login.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("LoginActivity", "Attempt to login with email/pw $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!it.isSuccessful)   return@addOnCompleteListener

                //else
                Log.d("LoginActivity", "Successfully logged in! ${it.result?.user?.uid}")
                val intent = Intent(this, LatestMessages::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or((Intent.FLAG_ACTIVITY_NEW_TASK)) //clears the stack or when you click back it goes back to the screenk
                startActivity(intent)


            }.addOnFailureListener {
                Log.d("LoginActivity", "Failed to login :( ${it.message}")
            }


        }

        back_login.setOnClickListener {
            finish()
        }
    }


}