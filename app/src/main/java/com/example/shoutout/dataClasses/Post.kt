package com.example.shoutout.dataClasses

import android.os.Parcelable
import com.example.shoutout.dataClasses.Opened
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val postId: String = "",
    val ownerId: String = "",
    val titleText: String = "",
    val imageUrI: String? = "",
    val contentText: String = "",
    val ownerName: String = "",
    val ownerType: String = "",
    val timeStamp: Long = 0L,
    val views: List<Opened> = listOf()
) : Parcelable