package com.rnt.videomeeting.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.rnt.videomeeting.R
import com.rnt.videomeeting.Utilities.Constants
import com.rnt.videomeeting.Utilities.PreferneceManager

class SignUpActivity : AppCompatActivity() {
    private var inputFirstName: EditText? = null
    private var inputLastName: EditText? = null
    private var inputEmail: EditText? = null
    private var inputPassowrd: EditText? = null
    private var inputConfirmPassword: EditText? = null

    private var buttonSignUp: MaterialButton? = null
    private var signUpProgressBar:ProgressBar?=null
    private var preferneceManager:PreferneceManager?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        preferneceManager= PreferneceManager(applicationContext)

        findViewById<ImageView>(R.id.imageBack).setOnClickListener {
            onBackPressed()
        }
        findViewById<TextView>(R.id.textSignIn).setOnClickListener {
            onBackPressed()
        }

        inputFirstName = findViewById(R.id.inputFirstName)
        inputLastName = findViewById(R.id.inputLastName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassowrd = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)
        signUpProgressBar = findViewById(R.id.signUpProgressBar)
        buttonSignUp = findViewById(R.id.buttonSignUp)

        buttonSignUp!!.setOnClickListener {

            if (inputFirstName!!.text.toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity@ this, "Enter first name", Toast.LENGTH_SHORT).show()
            } else if (inputLastName!!.text.toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity@ this, "Enter last name", Toast.LENGTH_SHORT).show()
            } else if (inputEmail!!.text.toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity@ this, "Enter email", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail!!.text.toString()).matches()) {
                Toast.makeText(SignUpActivity@ this, "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (inputPassowrd!!.text.toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity@ this, "Enter password", Toast.LENGTH_SHORT).show()
            } else if (inputConfirmPassword!!.text.toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity@ this, "Confirm your password", Toast.LENGTH_SHORT)
                    .show()
            } else if (!inputPassowrd!!.text.toString()
                    .equals(inputConfirmPassword!!.text.toString())
            ) {
                Toast.makeText(
                    SignUpActivity@ this,
                    "Password & confirm password must be same",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                signUp()
            }

        }
    }

    private fun signUp() {
        buttonSignUp!!.visibility=View.INVISIBLE
        signUpProgressBar!!.visibility=View.VISIBLE

        var database= FirebaseFirestore.getInstance()
        var user=HashMap<String, Any>()

        user[Constants.KEY_FIRST_NAME] = inputFirstName!!.text.toString()
        user[Constants.KEY_LAST_NAME] = inputLastName!!.text.toString()
        user[Constants.KEY_EMAIL] = inputEmail!!.text.toString()
        user[Constants.KEY_PASSWORD] = inputPassowrd!!.text.toString()

        database.collection(Constants.KEY_COLLECTION_USERS)
            .add(user)
            .addOnSuccessListener{
                preferneceManager!!.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                preferneceManager!!.putString(Constants.KEY_USER_ID,it.id!!)
                preferneceManager!!.putString(Constants.KEY_FIRST_NAME,inputFirstName!!.text.toString())
                preferneceManager!!.putString(Constants.KEY_LAST_NAME,inputLastName!!.text.toString())
                preferneceManager!!.putString(Constants.KEY_EMAIL,inputEmail!!.text.toString())
                preferneceManager!!.putString(Constants.KEY_PASSWORD,inputPassowrd!!.text.toString())
                var intent=Intent(applicationContext, MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener {
                buttonSignUp!!.visibility=View.VISIBLE
                signUpProgressBar!!.visibility=View.INVISIBLE
                Toast.makeText(SignInActivity@this,"Error adding user "+ it.message, Toast.LENGTH_SHORT).show()

            }

    }
}