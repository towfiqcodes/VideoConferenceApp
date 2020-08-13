package com.rnt.videomeeting.model

import java.io.Serializable

 class User(
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var fcm_token: String?
):Serializable  {


}