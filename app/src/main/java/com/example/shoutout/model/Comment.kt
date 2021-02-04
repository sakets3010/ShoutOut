package com.example.shoutout.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val commentId: String = "",
    val author: String = "",
    val comment: String = "",
    val postId: String = "",
    val timeStamp: Long = 0L,
) : Parcelable