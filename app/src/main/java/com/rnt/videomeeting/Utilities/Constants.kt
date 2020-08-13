package com.rnt.videomeeting.Utilities

class Constants {

    companion object {


        const val KEY_COLLECTION_USERS = "users"
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
        const val KEY_USER_ID = "user_id"
        const val KEY_FCM_TOKEN = "fcm_token"

        const val KEY_PREFERENCE_NAME = "videoMeetingPreference"
        const val KEY_IS_SIGNED_IN = "isSignIn"


         const val REMOTE_MSG_AUTHORIZATION = "Authorization"
        const val REMOTE_MSG_CONTENT_TYPE = "Content-Type"
         const val REMOTE_MSG_TYPE = "type"
         const val REMOTE_MSG_INVITAION = "invitation"
         const val REMOTE_MSG_MEETING_TYPE = "meetingType"
        const val REMOTE_MSG_INVITER_TOKEN = "inviterToken"
         const val REMOTE_MSG_DATA = "data"
         const val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"

        fun getRemoteMessageHeaders(): HashMap<String, String> {
            var headers = HashMap<String, String>()
            headers[Constants.REMOTE_MSG_AUTHORIZATION] = "key=AAAAqenhaLc:APA91bGHR6dCk9xxiUN4gYHtm3MzbW-FP6kxJ4jdPv7UblQJGgVNFQNQj6rmuSg9ShBG_g-J-nD9sNYWqCV7CHVLQGzekrx9G_nEo6xY_jZ3vuzzJ0g6h1ALtk4K4GHGUkWFfe9LG3WC"
            headers[Constants.REMOTE_MSG_CONTENT_TYPE] = "application/json"

            return headers
        }

    }
}