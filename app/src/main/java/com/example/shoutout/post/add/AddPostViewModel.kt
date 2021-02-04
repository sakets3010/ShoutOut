package com.example.shoutout.post.add

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.model.EditItem
import com.example.shoutout.helper.Post
import com.example.shoutout.helper.Type
import com.example.shoutout.model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask

class AddPostViewModel @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {

    private lateinit var _name: String
    private lateinit var _type: String

    fun savePostToFirebaseDatabase(titleText: String, imageUrI: Uri?, contentText: String) {

        val uid = Firebase.auth.uid ?: throw Exception("User Null")

        val postRef = repository.getPost().document()
        val editRef = repository.getEditHistory(postRef.id)

        repository.getUserReference(uid).addSnapshotListener { snapshot, e ->

            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                val data = snapshot.toObject(User::class.java)
                if (data != null) {
                    _name = data.username
                    _type = getUserType(data.type)
                } else {
                    throw java.lang.Exception("null data fetched")
                }

            } else {
                Log.d("Add Post", "Current data: null")
            }
            val filePath = repository.getStorageReference()
                .child("${FirebaseDatabase.getInstance().reference.push().key}")

            //If there is an image attached
            if (imageUrI != null) {
                filePath.putFile(imageUrI)
                    .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                        return@Continuation filePath.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            postRef.set(
                                Post(
                                    postRef.id,
                                    uid,
                                    titleText,
                                    task.result.toString(),
                                    contentText,
                                    _name,
                                    _type,
                                    timeStamp = System.currentTimeMillis()
                                )
                            )
                        } else {
                            throw Exception("could not complete task")
                        }

                    }
            }
            //if there is not an image attached (just a text post)
            else {
                postRef.set(
                    Post(
                        postRef.id,
                        uid,
                        titleText,
                        contentText = contentText,
                        ownerName = _name,
                        ownerType = _type,
                        timeStamp = System.currentTimeMillis()
                    )
                )
            }

            editRef.add(EditItem(contentText,System.currentTimeMillis()))


        }


    }


    private fun getUserType(type: Long): String {

        return when (type) {
            Type.CLUBS -> "Club"
            Type.DEPARTMENT -> "Department"
            Type.REGIONAL_ASSOC -> "Regional Assoc"
            else -> throw Exception("unknown type")
        }


    }

}