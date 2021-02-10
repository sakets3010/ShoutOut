package com.example.shoutout.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reply(
    val author: String = "",
    val comment: String = "",
    val timeStamp: Long = 0L
) : Parcelable