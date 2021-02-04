package com.example.shoutout.helper

import java.text.SimpleDateFormat
import java.util.*

fun getDateTime(s: Long): String? {
    return try {
        val sdf = SimpleDateFormat("dd-MMM-yy,hh:mmaa", Locale.getDefault())
        val netDate = Date(s)
        sdf.format(netDate)
    } catch (e: Exception) {
        throw Exception(e.toString())
    }
}