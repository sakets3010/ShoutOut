package com.example.shoutout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.databinding.EditListItemBinding
import com.example.shoutout.helper.getDateTime
import com.example.shoutout.model.EditItem
import java.text.SimpleDateFormat
import java.util.*

class EditHistoryAdapter : RecyclerView.Adapter<EditHistoryAdapter.EditHistoryViewHolder>() {

    var data = listOf<EditItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditHistoryViewHolder {
        return EditHistoryViewHolder(
            EditListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: EditHistoryAdapter.EditHistoryViewHolder, position: Int) {
        val edit = data[position]

        with(holder.binding) {
            editContent.text = edit.content
            editTimestamp.text = getDateTime(edit.timeStamp)
        }

    }

    inner class EditHistoryViewHolder(val binding: EditListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

