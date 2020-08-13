package com.rnt.videomeeting.firebase

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rnt.videomeeting.Utilities.Constants
import com.rnt.videomeeting.activity.IncomingInvitationActivity

class MessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var type= message.data[Constants.REMOTE_MSG_TYPE]

        if(type!=null){
            if (type.equals(Constants.REMOTE_MSG_INVITAION)){
                var intent=Intent(applicationContext, IncomingInvitationActivity::class.java)
                intent.putExtra(Constants.REMOTE_MSG_MEETING_TYPE, message.data[Constants.REMOTE_MSG_MEETING_TYPE]
                )
                intent.putExtra(Constants.KEY_FIRST_NAME, message.data[Constants.KEY_FIRST_NAME]
                )
                intent.putExtra(Constants.KEY_LAST_NAME, message.data[Constants.KEY_LAST_NAME]
                )
                intent.putExtra(Constants.KEY_EMAIL, message.data[Constants.KEY_EMAIL]
                )

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }

    }
}