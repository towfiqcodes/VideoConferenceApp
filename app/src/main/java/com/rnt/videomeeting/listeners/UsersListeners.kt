package com.rnt.videomeeting.listeners

import com.rnt.videomeeting.model.User

public  interface UsersListeners {

  public  fun initiateVideoMeeting(user: User)

 public   fun initiateAudioMeeting(user: User)
}