package com.example.shoutout.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.ShoutApplication.Companion.globalRecyclerView
import com.example.shoutout.ShoutRepository
import com.example.shoutout.databinding.PostListItemBinding
import com.example.shoutout.helper.Post
import com.example.shoutout.helper.getDateTime
import com.example.shoutout.model.Opened
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class PostPagingAdapter : PagingDataAdapter<Post, PostPagingAdapter.PostViewHolder>(Companion) {

    lateinit var listener: (Post) -> Unit
    val repository = ShoutRepository()
    private val _uid = Firebase.auth.uid ?: throw java.lang.Exception("null uid")
    private val _viewList = arrayListOf<Opened>()

    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: throw Exception("null fetched")

        holder.setIsRecyclable(false)

        Log.d("viewed", "onBindViewHolder called")

        with(holder.binding) {
            titleText.text = post.titleText
            Log.d("viewed", post.titleText)
            matterText.text = post.contentText
            postDateText.text = getDateTime(post.timeStamp)
            author.text = post.ownerName
            authorCategory.text = post.ownerType
            postCard.setOnClickListener { listener(post) }

            if (post.imageUrI?.isNotEmpty() == true) {
                Picasso.get().load(post.imageUrI).into(featuredImage)
            }
            val initPosition =
                (globalRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            val lastPosition =
                (globalRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (position in initPosition..lastPosition) {
                addAccount(post.postId)
            }
        }


    }

    inner class PostViewHolder(val binding: PostListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        globalRecyclerView = recyclerView
    }

    private fun addAccount(postId: String) {
        repository.getVisits(postId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                _viewList.clear()
                for (snap in snapshot) {
                    val userId = snap.toObject(Opened::class.java)
                    _viewList.add(userId)
                }
            } else {
                throw Exception("null snapshot")
            }
            if (!_viewList.contains(Opened(_uid))) {
                Opened(_uid).let { repository.getVisits(postId).add(it) }
            }
        }
    }
}


