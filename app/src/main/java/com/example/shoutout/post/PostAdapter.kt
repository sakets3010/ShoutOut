package com.example.shoutout.post

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.databinding.PostListItemBinding
import com.example.shoutout.helper.Post
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

   // lateinit var listener: (Post) -> Unit

    var data = listOf<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = data[position]

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


    private fun getDateTime(s: Long): String? {
        return try {
            val sdf = SimpleDateFormat("dd MMM-yy", Locale.getDefault())
            val netDate = Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            throw Exception(e.toString())
        }
    }

    inner class PostViewHolder(val binding: PostListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}