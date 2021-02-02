package com.example.shoutout.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.databinding.PostListItemBinding
import com.example.shoutout.helper.Post
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class PostPagingAdapter : PagingDataAdapter<Post, PostViewHolder>(Companion) {

    // lateinit var listener: (Post) -> Unit

    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: throw Exception("null returned")

        holder.setIsRecyclable(false)

        with(holder.binding) {
            titleText.text = post.titleText
            matterText.text = post.contentText
            postDateText.text = getDateTime(post.timeStamp)
            author.text = post.ownerName
            authorCategory.text = post.ownerType
            if(post.imageUrI?.isNotEmpty() == true){
                Picasso.get().load(post.imageUrI).into(featuredImage)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}

private fun getDateTime(s: Long): String? {
    return try {
        val sdf = SimpleDateFormat("dd MMM-yy", Locale.getDefault())
        val netDate = Date(s)
        sdf.format(netDate)
    } catch (e: Exception) {
        throw Exception(e.toString())
    }
}

class PostViewHolder(
    val binding: PostListItemBinding
) : RecyclerView.ViewHolder(binding.root)