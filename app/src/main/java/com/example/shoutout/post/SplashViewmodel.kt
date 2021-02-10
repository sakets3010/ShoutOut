package com.example.shoutout.post

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashViewmodel @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {
    private var _detail: MutableLiveData<User> = MutableLiveData()
    val detail: LiveData<User>
        get() = _detail


    init {
        fetchUser()
    }

    private fun fetchUser() {
        val currentUser =  Firebase.auth.currentUser
        if (currentUser!= null) {
            repository.getUserReference(currentUser.uid).addSnapshotListener { snap, _ ->
                val details = snap?.toObject(User::class.java)
                _detail.value = details
            }
        }
    }
}