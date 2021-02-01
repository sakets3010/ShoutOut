package com.example.shoutout.post

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.helper.Post
import com.example.shoutout.helper.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class FeedViewmodel @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel() {

    private val _postList = arrayListOf<Post>()

    private val _uid = Firebase.auth.uid ?: throw Exception("User Null")

    private var _posts: MutableLiveData<List<Post>> = MutableLiveData()
    val posts: LiveData<List<Post>>
        get() = _posts

    private var _canAdd: MutableLiveData<Boolean> = MutableLiveData()
    val canAdd: LiveData<Boolean>
        get() = _canAdd

    init {
        addPostsToFeed()
        canAdd()
    }


    private fun addPostsToFeed() {
        repository.getPost().orderBy("timeStamp",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, e ->    //descending to fetch the latest post first
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

    private fun canAdd(){
        repository.getUserReference(_uid).addSnapshotListener{ snapshot,e->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.toObject(User::class.java)
                if (data != null) {
                    _canAdd.value = data.club
                }
                else{
                    throw Exception("cant determine org")
                }
            } else {
                Log.d("Feed", "Current data: null")
            }
        }
    }

    fun signUserOut() {
        Firebase.auth.signOut()
    }
}