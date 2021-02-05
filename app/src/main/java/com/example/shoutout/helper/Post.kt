package com.example.shoutout.helper

import android.os.Parcelable
import com.example.shoutout.model.Opened
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val postId: String = "",
    val ownerId: String = "",
    val titleText: String = "",
    val imageUrI: String? = "",
    val contentText: String = "",
    val ownerName: String ="",
    val ownerType: String ="",
    val timeStamp: Long = 0L,
    val upVotes: Int = 0,
    val downVotes: Int = 0
) : Parcelable