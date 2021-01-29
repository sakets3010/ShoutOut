package com.example.shoutout.login

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.User
import com.example.shoutout.helper.Type
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class DetailsViewmodel @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {

    fun setCategory(type: String): Long {
        return when (type) {
            "Club" -> Type.CLUBS
            "Department" -> Type.DEPARTMENT
            "Regional Assoc" -> Type.REGIONAL_ASSOC
            "General Body" -> Type.GENERAL_BODY
            else -> throw Exception("unknown type")
        }
    }

    fun determineWhetherClub(type: Long): Boolean {
        return when (type) {
            Type.CLUBS -> true
            Type.DEPARTMENT -> true
            Type.REGIONAL_ASSOC -> true
            Type.GENERAL_BODY -> false
            else -> throw Exception("unknown type")
        }
    }

    fun saveUserToFirebaseDatabase(username: String, type: Long, isClub: Boolean) {

        val uid = Firebase.auth.uid ?: ""
        val user = User(
            uid, username, type, isClub, true
        )

        repository.getUserReference(uid).set(user)

    }

}