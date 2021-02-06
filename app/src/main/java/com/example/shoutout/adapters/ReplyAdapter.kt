package com.example.shoutout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.databinding.CommentListItemBinding
import com.example.shoutout.helper.getDateTime
import com.example.shoutout.model.Reply

class ReplyAdapter : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    var data = listOf<Reply>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyViewHolder {
        return ReplyViewHolder(
            CommentListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ReplyAdapter.ReplyViewHolder, position: Int) {
        val userComment = data[position]

        with(holder.binding) {
            author.text = userComment.author
            comment.text = userComment.comment
            commentDate.text = getDateTime(userComment.timeStamp)
        }


    }

    inner class ReplyViewHolder(val binding: CommentListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}