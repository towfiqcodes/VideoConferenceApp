package com.rnt.videomeeting.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.rnt.videomeeting.R
import com.rnt.videomeeting.Utilities.Constants
import com.rnt.videomeeting.Utilities.PreferneceManager
import com.rnt.videomeeting.adapter.UserAdapter
import com.rnt.videomeeting.listeners.UsersListeners
import com.rnt.videomeeting.model.User
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.set

class MainActivity : AppCompatActivity(), UsersListeners {
    var textTitle: TextView? = null
    private var preferneceManager: PreferneceManager? = null
    private var users: MutableList<User>? = null
    private var userAdapter: UserAdapter? = null
    private var textErrorMessage: TextView? = null
    private var swipeRefreshLayout:SwipeRefreshLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferneceManager = PreferneceManager(applicationContext)


        textTitle = findViewById(R.id.textTitle)
        textTitle!!.text = String.format(
            "%s %s",
            preferneceManager!!.getString(Constants.KEY_FIRST_NAME),
            preferneceManager!!.getString(Constants.KEY_LAST_NAME)
        )
        findViewById<TextView>(R.id.textSignOut).setOnClickListener {
            signOut()
        }

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                sendFCMTokenToDatabase(it.result!!.token)
            }
        }


        var usersRecyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        textErrorMessage = findViewById(R.id.textErrorMessage)

        users = ArrayList()
        userAdapter = UserAdapter(users as ArrayList<User>, this)
        usersRecyclerView.adapter = userAdapter

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout!!.setOnRefreshListener(this::getUsers)
        /* val users: MutableList<User> =ArrayList()
         val firstName = "towfiqul"
         val lastName = "towfiqul"
         val email = "towfiqul"
         val token = "towfiqul"

         val user =
             User(firstName, lastName, email, token)
         users.add(user)*/

        getUsers()

    }

    fun getUsers() {
        swipeRefreshLayout!!.isRefreshing=true
        var database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener {
                swipeRefreshLayout!!.isRefreshing=false
                var myUserId = preferneceManager!!.getString(Constants.KEY_USER_ID)
                if (it.isSuccessful && it.result != null) {
                    users!!.clear()
                    for (documentSnapshot in it.result!!) {
                        if (myUserId.equals(documentSnapshot.id)) {
                            continue
                        }
                        var firstName=documentSnapshot.getString(Constants.KEY_FIRST_NAME)
                        var lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME)
                        var email = documentSnapshot.getString(Constants.KEY_EMAIL)
                        var fcm_token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN)
                         var user = User(firstName,lastName,email,fcm_token)
                        users!!.add(user)
                        if (users!!.isNotEmpty()) {
                            userAdapter!!.notifyDataSetChanged()
                        } else {
                            textErrorMessage!!.text = String.format("%s", "No users available")
                            textErrorMessage!!.visibility = View.VISIBLE
                        }
                    }

                } else {
                    textErrorMessage!!.text = String.format("%s", "No users available")
                    textErrorMessage!!.visibility = View.VISIBLE
                }
            }

    }


    fun sendFCMTokenToDatabase(token: String) {
        var database = FirebaseFirestore.getInstance()
        var documentReference =database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferneceManager!!.getString(Constants.KEY_USER_ID)!!
        )

        documentReference!!.update(Constants.KEY_FCM_TOKEN, token)
            .addOnFailureListener {
                Toast.makeText(
                    MainActivity@ this,
                    "Unable to send token: " + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    fun signOut() {
        Toast.makeText(MainActivity@ this, "Signing Out..... ", Toast.LENGTH_SHORT).show()
        var database = FirebaseFirestore.getInstance()
        var documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferneceManager!!.getString(Constants.KEY_USER_ID)!!
        )
        val hashMap =
            HashMap<String, Any>()
        hashMap[Constants.KEY_FCM_TOKEN] = FieldValue.delete()

        documentReference!!.update(hashMap)
            .addOnSuccessListener {
                preferneceManager!!.clearPreferences()
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    MainActivity@ this,
                    "Unable to sign out " + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }



    override fun initiateVideoMeeting(user: User) {
        if (user?.fcm_token != null && user?.fcm_token?.trim()!!.isNotEmpty() ) {
           var intent=Intent(applicationContext, OutgoingMeetingInvitationActivity::class.java)
            intent.putExtra("user",user)
            intent.putExtra("type","video")
            startActivity(intent)

        } else {
            Toast.makeText(
                MainActivity@ this,
                user.firstName + "" + user.lastName + "is not available for meeting",
                Toast.LENGTH_SHORT
            ).show()


        }
    }

    override fun initiateAudioMeeting(user: User) {
        if (user.fcm_token != null && user?.fcm_token?.trim()!!.isNotEmpty()!!) {
            Toast.makeText(
                MainActivity@ this,
                "Audio meeting with " + user.firstName + "" + user.lastName,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                MainActivity@ this,
                user.firstName + "" + user.lastName + "is not available for meeting",
                Toast.LENGTH_SHORT
            ).show()


        }
    }

}