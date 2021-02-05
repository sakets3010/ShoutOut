package com.example.shoutout

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShoutApplication: Application(){
    companion object{
        lateinit var globalRecyclerView: RecyclerView
    }
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}