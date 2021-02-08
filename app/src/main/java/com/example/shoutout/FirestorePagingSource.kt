package com.example.shoutout

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.shoutout.helper.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestorePagingSource(
    private val db: FirebaseFirestore,
    ) : PagingSource<QuerySnapshot, Post>() {

    companion object {
        const val PAGING_LIMIT = 10L
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {

            val currentPage =
                params.key ?: db.collection("Posts")
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .limit(PAGING_LIMIT)
                    .get()
                    .await()

            Log.d("page","value paging:${currentPage.toObjects(Post::class.java)}")

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("Posts")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .limit(PAGING_LIMIT)
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(Post::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}

