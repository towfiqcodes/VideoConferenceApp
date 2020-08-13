package com.rnt.videomeeting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rnt.videomeeting.R
import com.rnt.videomeeting.listeners.UsersListeners
import com.rnt.videomeeting.model.User

class UserAdapter(val userList: MutableList<User>,  val usersListeners: UsersListeners ) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textFirstChar = itemView.findViewById<TextView>(R.id.textFirstChar)
        var textUsername = itemView.findViewById<TextView>(R.id.textUsername)
        var textEmail = itemView.findViewById<TextView>(R.id.textEmail)
        var imageAudioMeeting = itemView.findViewById<ImageView>(R.id.imageAudioMeeting)
        var imageVideoMeeting = itemView.findViewById<ImageView>(R.id.imageVideoMeeting)


        fun setUserData(
            user: User,
            usersListeners: UsersListeners
        ) {
            textFirstChar.text = user.firstName!!.substring(0, 1)
            textUsername.text = String.format("%s %s", user.firstName, user.lastName)
            textEmail.text = user.email

            imageAudioMeeting.setOnClickListener {
             usersListeners.initiateAudioMeeting(user)
            }
            imageVideoMeeting.setOnClickListener {
                usersListeners.initiateVideoMeeting(user)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_container_user,
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(userList[position], usersListeners)


    }
}