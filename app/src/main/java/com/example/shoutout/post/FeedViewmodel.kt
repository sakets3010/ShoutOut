package com.example.shoutout.post

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.helper.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FeedViewmodel @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {


    private val _postList = arrayListOf<Post>()

    private var _posts: MutableLiveData<List<Post>> = MutableLiveData()
    val posts: LiveData<List<Post>>
        get() = _posts

    init {
        addPostsToFeed()
    }


    private fun addPostsToFeed() {
        repository.getPost().addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _postList.clear()
              for(snap in snapshot){
                  val post = snap.toObject(Post::class.java)
                  _postList.add(post)
              }
            }
            _posts.value = _postList
        }

    }

    fun signUserOut() {
        Firebase.auth.signOut()
    }
}