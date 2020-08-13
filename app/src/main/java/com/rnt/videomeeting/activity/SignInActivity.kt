package com.rnt.videomeeting.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.rnt.videomeeting.R
import com.rnt.videomeeting.Utilities.Constants
import com.rnt.videomeeting.Utilities.PreferneceManager


class SignInActivity : AppCompatActivity() {
    private var inputEmail:EditText?=null
    private var inputPassword:EditText?=null
    private var buttonSignIn: MaterialButton? = null

    private var signInProgressBar:ProgressBar?=null
    private var preferneceManager: PreferneceManager?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        preferneceManager= PreferneceManager(applicationContext)
        if(preferneceManager!!.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.textSignUp).setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
        /*   var database=FirebaseFirestore.getInstance()
           var user=HashMap<String, String>()

           user["first_name"] = "John"
           user["last_name"] = "Doe"
           user["email"] = "john.doe@gmail.com"
           database.collection("users")
               .add(user)
               .addOnSuccessListener{
                   Toast.makeText(SignInActivity@this, "User Inserted", Toast.LENGTH_SHORT).show()
               }
               .addOnFailureListener {
                   Toast.makeText(SignInActivity@this,"Error adding user "+ it.message, Toast.LENGTH_SHORT).show()

               }*/
        inputEmail=findViewById(R.id.inputEmail)
        inputPassword=findViewById(R.id.inputPassword)
        buttonSignIn=findViewById(R.id.buttonSignIn)
        signInProgressBar=findViewById(R.id.signInProgressBar)

        buttonSignIn!!.setOnClickListener {

            if (inputEmail!!.text.toString().trim().isEmpty()){
                makeText(SignInActivity@ this, "Enter email", LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail!!.text.toString()).matches()) {
              makeText(SignInActivity@ this, "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (inputPassword!!.text.toString().trim().isEmpty()) {
                makeText(SignInActivity@ this, "Enter password", Toast.LENGTH_SHORT).show()
            }else{
                signIn()
            }
        }



    }

    private fun signIn() {
        signInProgressBar!!.visibility= View.VISIBLE
        buttonSignIn!!.visibility= View.INVISIBLE
        var database= FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, inputEmail!!.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD,inputPassword!!.text.toString())
            .get()
            .addOnCompleteListener {
             if (it.isSuccessful && it.result!=null && it.result!!.documents.size>0){
                 var documentSnapshot=it.result!!.documents[0]

                 preferneceManager!!.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                 preferneceManager!!.putString(Constants.KEY_USER_ID,documentSnapshot.id!!)
                 preferneceManager!!.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME)!!)
                 preferneceManager!!.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME)!!)
                 preferneceManager!!.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL)!!)
                 var intent=Intent(applicationContext, MainActivity::class.java)
                 intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 startActivity(intent)
             }else{
                 signInProgressBar!!.visibility= View.INVISIBLE
                 buttonSignIn!!.visibility= View.VISIBLE
                 makeText(SignInActivity@ this, "Unable to sign in", Toast.LENGTH_SHORT).show()
             }
            }
    }
}