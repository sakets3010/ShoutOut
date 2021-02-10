package com.example.shoutout.post.edit

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.dataClasses.Post
import com.example.shoutout.dataClasses.EditItem
import com.example.shoutout.dataClasses.Opened

class EditPostViewModel @ViewModelInject constructor(
    private val repository: ShoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _content: MutableLiveData<String> = MutableLiveData()
    val content: LiveData<String>
        get() = _content

    private val _openedList = arrayListOf<Opened>()

    private var _opened: MutableLiveData<Int> = MutableLiveData()
    val opened: LiveData<Int>
        get() = _opened

    private var _viewed: MutableLiveData<Int> = MutableLiveData()
    val viewed: LiveData<Int>
        get() = _viewed

    private var _reacted: MutableLiveData<Int> = MutableLiveData()
    val reacted: LiveData<Int>
        get() = _reacted


    init {
        val post =
            savedStateHandle.get<Post>("post") ?: throw java.lang.Exception("null post object")
        getContent(postId = post.postId)
        getOpenedCount(postId = post.postId)
        getViewCount(postId = post.postId)
        getReactCount(postId = post.postId)
    }


    private fun getOpenedCount(postId: String) {
        repository.getVisits(postId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                _openedList.clear()
                for (snap in snapshot) {
                    val userId = snap.toObject(Opened::class.java)
                    _openedList.add(userId)
                }
            } else {
                Log.d("Edit Post", "Current data: null")
            }

            _opened.value = _openedList.size
        }

    }

    private fun getViewCount(postId: String) {
        repository.getPostReference(postId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val post =
                    snapshot.toObject(Post::class.java) ?: throw Exception("null snapshot value")
                _viewed.value = post.views.size

            } else {
                Log.d("Edit Post", "Current data: null")
            }


        }

    }

    private fun getReactCount(postId: String) {
        var count = 0
        repository.getReacts(postId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (snap in snapshot) {
                    count++
                }
            } else {
                Log.d("Edit Post", "Current data: null")
            }
            _reacted.value = count
        }
    }

    fun updateHistory(postId: String, text: String) {
        repository.getEditHistory(postId).add(EditItem(text, System.currentTimeMillis()))
    }


    fun updateContent(postId: String, text: String) {
        repository.getPostReference(postId).update(
            mapOf(
                "contentText" to text,
            )
        )
    }

    private fun getContent(postId: String) {
        repository.getPostReference(postId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                val data = snapshot.toObject(Post::class.java)
                if (data != null) {
                    _content.value = data.contentText
                } else {
                    throw java.lang.Exception("null data retrieved")
                }

            } else {
                Log.d("Edit Post", "Current data: null")
            }
        }
    }

}