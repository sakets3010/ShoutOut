package com.example.shoutout.post.comment

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.model.Comment
import com.example.shoutout.model.Reply
import com.example.shoutout.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase

class ReplyViewModel @ViewModelInject constructor(
    private val repository: ShoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _replyList = listOf<Reply>()

    private var _replies: MutableLiveData<List<Reply>> = MutableLiveData()
    val replies: LiveData<List<Reply>>
        get() = _replies

    init {
        val comment =
            savedStateHandle.get<Comment>("comment")
                ?: throw java.lang.Exception("null comment object")
        addReplies(comment.commentId)
    }

    private lateinit var _name: String
    private val _uid = Firebase.auth.uid ?: throw Exception("User Null")

    fun saveReplyToFirebaseDatabase(comment: String, commentId: String) {

        val replyRef = repository.getCommentReference(commentId)

        repository.getUserReference(_uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                val data = snapshot.toObject(User::class.java)
                if (data != null) {
                    _name = data.username
                } else {
                    throw java.lang.Exception("null data fetched")
                }

            } else {
                Log.d("Post Detail", "Current data: null")
            }

            replyRef.update(
                "replies",
                FieldValue.arrayUnion(Reply(_name, comment, System.currentTimeMillis()))
            )


        }
    }

    private fun addReplies(commentId: String) {
        repository.getCommentReference(commentId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val comment = snapshot.toObject(Comment::class.java)
                if (comment != null) {
                    _replyList = comment.replies
                } else {
                    throw java.lang.Exception("null comment retrieved")
                }

            } else {
                Log.d("Reply", "Current data: null")
            }
            _replies.value = _replyList
        }

    }
}

