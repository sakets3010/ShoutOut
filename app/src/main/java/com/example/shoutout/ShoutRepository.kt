package com.example.shoutout

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Provides


class ShoutRepository  {

    fun getPostReference(id:String): DocumentReference {
        return Firebase.firestore.collection("Posts").document(id)
    }

    fun getPost(): CollectionReference {
        return Firebase.firestore.collection("Posts")
    }

    fun getUserReference(uid:String): DocumentReference {
        return Firebase.firestore.collection("Users").document(uid)
    }

    fun getUser(): CollectionReference {
        return Firebase.firestore.collection("Users")
    }

    fun getStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference.child("post_images")
    }

}