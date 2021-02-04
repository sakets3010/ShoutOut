package com.example.shoutout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.databinding.PostListItemBinding
import com.example.shoutout.helper.Post
import com.example.shoutout.helper.getDateTime
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class PostPagingAdapter : PagingDataAdapter<Post, PostPagingAdapter.PostViewHolder>(Companion) {

    lateinit var listener: (Post) -> Unit

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

        with(holder.binding) {
            titleText.text = post.titleText
            matterText.text = post.contentText
            postDateText.text = getDateTime(post.timeStamp)
            author.text = post.ownerName
            authorCategory.text = post.ownerType
            postCard.setOnClickListener { listener(post) }
            if (post.imageUrI?.isNotEmpty() == true) {
                Picasso.get().load(post.imageUrI).into(featuredImage)
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
}


