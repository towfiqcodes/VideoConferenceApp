package com.rnt.videomeeting.activity

import android.os.Bundle
import android.os.ResultReceiver
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.rnt.videomeeting.R
import com.rnt.videomeeting.Utilities.Constants
import com.rnt.videomeeting.Utilities.Constants.Companion.getRemoteMessageHeaders
import com.rnt.videomeeting.Utilities.PreferneceManager
import com.rnt.videomeeting.model.User
import com.rnt.videomeeting.network.ApiClient
import com.rnt.videomeeting.network.ApiClient.getClient
import com.rnt.videomeeting.network.ApiService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class OutgoingMeetingInvitationActivity : AppCompatActivity() {
    private var imageMeetingType:ImageView?=null
    private var imageStopInvitation:ImageView?=null
    private var textFirstChar:TextView?=null
    private var textUsername:TextView?=null
    private var textEmail:TextView?=null
    private var user:User?=null
    private var preferneceManager:PreferneceManager?=null
    private var inviterToken: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outgoing_meeting_invitation)


        preferneceManager= PreferneceManager(applicationContext)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful && it.result!=null){
                inviterToken= it.result!!.token
            }
        }

        imageMeetingType=findViewById<ImageView>(R.id.imageMeetingType)
        imageStopInvitation=findViewById<ImageView>(R.id.imageStopInvitation)
        var meetingType=intent.getStringExtra("type")
        if (meetingType!=null){
            if (meetingType == "video"){
                imageMeetingType!!.setImageResource(R.drawable.ic_video)
            }
        }

        textFirstChar=findViewById(R.id.textFirstChar)
        textUsername=findViewById(R.id.textUsername)
        textEmail=findViewById(R.id.textEmail)
        user = intent.getSerializableExtra("user") as User?
        if (user !=null){
            textFirstChar!!.text=user!!.firstName!!.substring(0,1)
            textUsername!!.text= String.format("%s %s", user!!.firstName, user!!.lastName)
            textEmail!!.text=user!!.email
        }

       imageStopInvitation!!.setOnClickListener {
           onBackPressed()
       }

        if (meetingType!=null && user !=null){
            initialMeeting(meetingType, user!!.fcm_token!!)
        }

    }

    fun initialMeeting(meetingType:String, receiverToken:String){
        try {
            var tokens=JSONArray()
            tokens.put(receiverToken)

          var body =JSONObject()
          var data =JSONObject()

            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITAION)
            data.put(Constants.REMOTE_MSG_MEETING_TYPE,meetingType)
            data.put(Constants.KEY_FIRST_NAME, preferneceManager?.getString(Constants.KEY_FIRST_NAME))
            data.put(Constants.KEY_LAST_NAME, preferneceManager?.getString(Constants.KEY_LAST_NAME))
            data.put(Constants.KEY_EMAIL, preferneceManager?.getString(Constants.KEY_EMAIL))
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN,inviterToken)

            body.put(Constants.REMOTE_MSG_DATA, data)
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens)
            sendRemoteMessage(body.toString(),Constants.REMOTE_MSG_INVITAION)

        }catch (exception:Exception){
            Toast.makeText(applicationContext, exception.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }



    fun sendRemoteMessage(remoteMessageBody:String, type:String){
        ApiClient.getClient()!!.create(ApiService::class.java).sendRemoteMssage(
            getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext,"Invitation sent successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

            override fun onFailure(
                call: Call<String?>,
                t: Throwable
            ) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }


}