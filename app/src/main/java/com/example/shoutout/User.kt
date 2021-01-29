package com.example.shoutout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val username: String = "",
    val type: Long = 103L,
    val isClub: Boolean = false,
    val profileSet: Boolean = false
) : Parcelable