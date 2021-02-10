package com.example.shoutout

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ShoutRepository  {


    fun getUserId() = Firebase.auth.uid ?: throw Exception("User Null")

    fun getPostReference(id:String): DocumentReference {
        return Firebase.firestore.collection("Posts").document(id)
    }

    fun getEditHistory(id:String): CollectionReference {
        return Firebase.firestore.collection("Posts").document(id).collection("EditHistory")
    }

    fun getVisits(id:String): CollectionReference {
        return Firebase.firestore.collection("Posts").document(id).collection("Opened")
    }

    fun getReacts(id:String): CollectionReference {
        return Firebase.firestore.collection("Posts").document(id).collection("Reacts")
    }

    fun getPost(): CollectionReference {
        return Firebase.firestore.collection("Posts")
    }

    fun getUserReference(uid:String): DocumentReference {
        return Firebase.firestore.collection("Users").document(uid)
    }

    fun getStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().reference.child("post_images")
    }

    fun getComment(): CollectionReference {
        return Firebase.firestore.collection("Comments")
    }

    fun getCommentReference(commentId:String): DocumentReference {
        return Firebase.firestore.collection("Comments").document(commentId)
    }


}