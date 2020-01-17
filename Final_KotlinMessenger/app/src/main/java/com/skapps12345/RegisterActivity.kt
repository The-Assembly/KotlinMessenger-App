package com.skapps12345

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        button_register.setOnClickListener {

            performRegister()

        }



        signin_register.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java )
            startActivity(intent)
        }


    }

    private fun performRegister() {

        val username  = username_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()


        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is: " + email)
        Log.d("RegisterActivity", "Password is: $password")

        //Firebase authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d("RegisterActivity", "Successfully created user with uid: ${it.result?.user?.uid} ")
                saveUserToFirebaseDatabase()

            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Fail to create user: ${it.message}")
                Toast.makeText(this, "Fail to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref =  FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_register.text.toString())
        Log.d("RegisterActivity", "Saving user to database...")
        ref.setValue(user).addOnSuccessListener {
            Log.d("RegisterActivity", "Finally the user is saved to database")

            val intent = Intent(this, LatestMessages::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or((Intent.FLAG_ACTIVITY_NEW_TASK)) //clears the stack or when you click back it goes back to the screenk
            startActivity(intent)


        }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed saving user to database")
            }
    }
}


@Parcelize
class User(val uid: String, val username: String): Parcelable {
    constructor() : this("", "")
}
