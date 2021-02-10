package com.example.shoutout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.R
import com.example.shoutout.ShoutRepository
import com.example.shoutout.databinding.PostListItemBinding
import com.example.shoutout.dataClasses.Post
import com.example.shoutout.helper.getDateTime
import com.squareup.picasso.Picasso


class PostPagingAdapter : PagingDataAdapter<Post, PostPagingAdapter.PostViewHolder>(Companion) {

    lateinit var listener: (Post) -> Unit

    var viewListener: ((String) -> Unit?)? = null

    val repository = ShoutRepository()

    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }


        lateinit var shoutRecyclerView: RecyclerView

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
            ?: throw Exception(holder.itemView.context.getString(R.string.null_exception))

        holder.setIsRecyclable(false)


        with(holder.binding) {
            titleText.text = post.titleText
            matterText.text = post.contentText
            postDateText.text = getDateTime(post.timeStamp)
            author.text = post.ownerName
            authorCategory.text = post.ownerType
            postCard.setOnClickListener { listener(post) }
            if (post.imageUrI?.isNotEmpty()
                    ?: throw java.lang.Exception(holder.itemView.context.getString(R.string.null_exception))
            ) {
                Picasso.get().load(post.imageUrI).into(featuredImage)
            }
            if ((shoutRecyclerView.layoutManager as LinearLayoutManager).isViewPartiallyVisible(
                    holder.binding.postHolder,
                    true,
                    false
                )
            ) {
                viewListener?.let { it(post.postId) }
                viewListener = null
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
        shoutRecyclerView = recyclerView
    }


}


