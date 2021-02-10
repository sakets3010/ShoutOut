package com.example.shoutout.post.feed

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.shoutout.FirestorePagingSource
import com.example.shoutout.ShoutRepository
import com.example.shoutout.dataClasses.Post
import com.example.shoutout.dataClasses.Opened
import com.example.shoutout.dataClasses.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FeedViewmodel @ViewModelInject constructor(
    private val repository: ShoutRepository
) : ViewModel(), LifecycleObserver {

    private val _postList = arrayListOf<Post>()

    private val _uid = repository.getUserId()

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


    fun postOpened(postId: String) {
        repository.getVisits(postId).add(Opened(_uid))
    }


    private fun addPostsToFeed() {
        repository.getPost()
            .addSnapshotListener { snapshot, e ->    //descending to fetch the latest post first
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _postList.clear()
                    for (snap in snapshot) {
                        val post = snap.toObject(Post::class.java)
                        _postList.add(post)
                    }
                } else {
                    Log.d("Feed", "Current data: null")
                }
                _posts.value = _postList
            }

    }

    val flow = Pager(PagingConfig(10)) {
        FirestorePagingSource(FirebaseFirestore.getInstance())
    }.flow.cachedIn(viewModelScope)


    fun updateViews(postId: String) {
        repository.getPostReference(postId).update("views", FieldValue.arrayUnion(Opened(_uid)))
    }

    private fun canAdd() {
        repository.getUserReference(_uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.toObject(User::class.java) ?: throw Exception("null retrieved")
                _canAdd.value = data.club
            } else {
                Log.d("Feed", "Current data: null")
            }
        }
    }

    fun signUserOut() {
        Firebase.auth.signOut()
    }
}