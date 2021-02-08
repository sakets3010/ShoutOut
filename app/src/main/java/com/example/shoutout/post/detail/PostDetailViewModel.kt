package com.example.shoutout.post.detail

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shoutout.R
import com.example.shoutout.ShoutRepository
import com.example.shoutout.helper.Post
import com.example.shoutout.helper.Type
import com.example.shoutout.model.Comment
import com.example.shoutout.model.Emoji
import com.example.shoutout.model.Reply
import com.example.shoutout.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class PostDetailViewModel @ViewModelInject constructor(
    private val repository: ShoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private lateinit var _name: String
    private val _uid = Firebase.auth.uid ?: throw Exception("User Null")


    private val _commentList = arrayListOf<Comment>()
    private val _replyList = arrayListOf<Reply>()

    private var _comments: MutableLiveData<List<Comment>> = MutableLiveData()
    val comments: LiveData<List<Comment>>
        get() = _comments

    private var _react: MutableLiveData<Long> = MutableLiveData()
    val react: LiveData<Long>
        get() = _react

    private var _commentCount: MutableLiveData<Int> = MutableLiveData()
    val commentCount: LiveData<Int>
        get() = _commentCount

    private var _owner: MutableLiveData<Boolean> = MutableLiveData()
    val owner: LiveData<Boolean>
        get() = _owner


    init {
        val post =
            savedStateHandle.get<Post>("post") ?: throw java.lang.Exception("null post object")
        checkOwnership(post.ownerId)
        addComments(postId = post.postId)
        fetchEmoji(post.postId)
    }


    fun saveCommentToFirebaseDatabase(comment: String, postId: String) {

        val commentRef = repository.getComment().document()

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

            commentRef.set(
                Comment(
                    commentRef.id,
                    _name,
                    comment,
                    postId,
                    System.currentTimeMillis()
                )
            )
        }


    }

    private fun fetchEmoji(postId: String) {
        repository.getReacts(postId).document(_uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.toObject(Emoji::class.java)
                    ?: throw java.lang.Exception("null retrieved")

            _react.value = data.react.getValue(_uid)

            } else {
                Log.d("Post Detail", "Current data: null")
            }
        }


    }

    fun addEmoji(postId: String, Type: Long) {
        val emoji = HashMap<String, Long>()
        emoji[_uid] = Type
        repository.getReacts(postId).document(_uid).set(Emoji(emoji))
    }


    private fun checkOwnership(ownerId: String) {
        _owner.value = Firebase.auth.uid.equals(ownerId)
    }

    fun deletePost(postId: String) {
        repository.getPostReference(postId).delete()
    }

    private fun addComments(postId: String) {
        repository.getComment().orderBy("timeStamp", Query.Direction.ASCENDING)
            .whereEqualTo("postId", postId).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _commentList.clear()
                    _replyList.clear()
                    for (snap in snapshot) {
                        val comment = snap.toObject(Comment::class.java)
                        comment.replies.forEach {
                            _replyList.add(it)
                        }
                        _commentList.add(comment)
                    }
                } else {
                    Log.d("Post Detail", "Current data: null")
                }
                _comments.value = _commentList
                _commentCount.value = _commentList.size + _replyList.size
            }

    }

    fun getEmojiType(context:Context,type: Long): String {
        return when (type) {
            Type.LIKE -> context.getString(R.string.emoji1)
            Type.HEART -> context.getString(R.string.emoji2)
            Type.WOW -> context.getString(R.string.emoji3)
            Type.LAUGH -> context.getString(R.string.emoji4)
            Type.SAD -> context.getString(R.string.emoji5)
            else -> throw java.lang.Exception("undetermined type")
        }
    }


}