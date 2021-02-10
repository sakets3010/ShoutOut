package com.example.shoutout.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Opened(
    val userId: String = "",
    ) : Parcelable