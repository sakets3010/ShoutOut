package com.example.shoutout.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class EditItem(
    val content: String = "",
    val timeStamp: Long = 0L,
) : Parcelable
