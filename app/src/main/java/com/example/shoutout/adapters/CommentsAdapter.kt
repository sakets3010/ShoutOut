package com.example.shoutout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.dataClasses.Comment
import com.example.shoutout.databinding.CommentListItemBinding
import com.example.shoutout.helper.getDateTime

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    lateinit var replyListener: (Comment) -> Unit

    var data = listOf<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            CommentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val userComment = data[position]

        with(holder.binding) {
            author.text = userComment.author
            comment.text = userComment.comment
            commentDate.text = getDateTime(userComment.timeStamp)
            commentsLayout.setOnClickListener {
                replyListener(userComment)
            }
            val replyAdapter = ReplyAdapter()
            replyRecycler.adapter = replyAdapter
            replyAdapter.data = userComment.replies
        }
    }

    inner class CommentsViewHolder(val binding: CommentListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

