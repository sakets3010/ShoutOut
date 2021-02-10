package com.example.shoutout.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Emoji(
    val react: HashMap<String,Long> = hashMapOf()
) : Parcelable