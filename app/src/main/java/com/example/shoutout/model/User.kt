package com.example.shoutout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val username: String = "",
    val type: Long = 103L,
    val club: Boolean = false,
    val registrationToken:String = "",
    val profileSet: Boolean = false
) : Parcelable