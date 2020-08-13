package com.rnt.videomeeting.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.rnt.videomeeting.R
import com.rnt.videomeeting.Utilities.Constants

class IncomingInvitationActivity : AppCompatActivity() {
    private var imageMeetingType:ImageView?=null
    private var imageStopInvitation:ImageView?=null
    private var textFirstChar: TextView?=null
    private var textUsername: TextView?=null
    private var textEmail: TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_invitation)

        imageMeetingType=findViewById(R.id.imageMeetingType)

        var meetingType=intent.getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE)
        if (meetingType!=null){
            if (meetingType == "video"){
                imageMeetingType!!.setImageResource(R.drawable.ic_video)
            }
        }
        textFirstChar=findViewById(R.id.textFirstChar)
        textUsername=findViewById(R.id.textUsername)
        textEmail=findViewById(R.id.textEmail)
        var firstName=intent.getStringExtra(Constants.KEY_FIRST_NAME)
        if(firstName!=null){
            textFirstChar!!.text=firstName!!.substring(0,1)
        }
        textUsername!!.text= String.format("%s %s", firstName, intent.getStringExtra(Constants.KEY_LAST_NAME))
        textEmail!!.text=intent.getStringExtra(Constants.KEY_EMAIL)
    }
}