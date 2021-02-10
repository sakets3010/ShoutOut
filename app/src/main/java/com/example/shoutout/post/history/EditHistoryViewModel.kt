package com.example.shoutout.post.history

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shoutout.ShoutRepository
import com.example.shoutout.dataClasses.EditItem
import com.example.shoutout.dataClasses.Post
import com.google.firebase.firestore.Query

class EditHistoryViewModel @ViewModelInject constructor(
    private val repository: ShoutRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _editList = arrayListOf<EditItem>()

    private var _history: MutableLiveData<List<EditItem>> = MutableLiveData()
    val history: LiveData<List<EditItem>>
        get() = _history

    init {
        val post =
            savedStateHandle.get<Post>("post") ?: throw java.lang.Exception("null post object")
        getHistory(post.postId)
    }

    private fun getHistory(postId: String) {
       repository.getEditHistory(postId).orderBy("timeStamp",Query.Direction.DESCENDING).addSnapshotListener { snapshot, e ->
           if (e != null) {
               return@addSnapshotListener
           }
           if (snapshot != null) {
               _editList.clear()
               for (snap in snapshot) {
                   val editItem = snap.toObject(EditItem::class.java)
                   _editList.add(editItem)

               }
           }else {
               Log.d("Edit History", "Current data: null")
           }
           _history.value = _editList
       }
    }

}