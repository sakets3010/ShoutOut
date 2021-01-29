package com.example.shoutout

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Provides


class ShoutRepository  {

    fun getUserReference(uid:String): DocumentReference {
        return Firebase.firestore.collection("Users").document(uid)
    }
    fun getUser(): CollectionReference {
        return Firebase.firestore.collection("Users")
    }

}