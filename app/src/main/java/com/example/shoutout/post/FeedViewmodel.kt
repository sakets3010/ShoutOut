package com.example.shoutout.post

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FeedViewmodel  @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {

    fun signUserOut() {
        Firebase.auth.signOut()
    }
}