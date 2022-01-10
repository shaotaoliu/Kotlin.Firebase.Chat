package com.example.kotlinchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.example.kotlinchat.model.*
import com.example.kotlinchat.R

class MessageAdapter(val context: Context, val messages: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvSentMessage: TextView = itemView.findViewById(R.id.tvSentMessage)
    }

    class ReceivedMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvReceivedMessage: TextView = itemView.findViewById(R.id.tvReceivedMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)
            return ReceivedMessageViewHolder(view)
        }
        else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            return SentMessageViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (FirebaseAuth.getInstance().currentUser?.uid == message.senderId) {
            return 1
        }
        else {
            return 2
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder.javaClass == SentMessageViewHolder::class.java) {
            val viewHolder = holder as SentMessageViewHolder
            holder.tvSentMessage.text = message.message
        }
        else {
            val viewHolder = holder as ReceivedMessageViewHolder
            holder.tvReceivedMessage.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}