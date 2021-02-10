package com.example.shoutout.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val username: String = "",
    val type: Long = 103L,
    val club: Boolean = false,
    val profileSet: Boolean = false
) : Parcelable