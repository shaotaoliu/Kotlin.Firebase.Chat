package com.example.kotlinchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinchat.model.*
import com.example.kotlinchat.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MessageAdapter(val context: Context, val messages: ArrayList<Message>, val receiverName: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SentMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvSentMessage: TextView = itemView.findViewById(R.id.tvSentMessage)
        val tvSentDt: TextView = itemView.findViewById(R.id.tvSentDt)
        val tvSenderInitial: TextView = itemView.findViewById(R.id.tvSenderInitial)
    }

    class ReceivedMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvReceivedMessage: TextView = itemView.findViewById(R.id.tvReceivedMessage)
        val tvReceivedDt: TextView = itemView.findViewById(R.id.tvReceivedDt)
        val tvReceiverInitial: TextView = itemView.findViewById(R.id.tvReceiverInitial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)
            ReceivedMessageViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            SentMessageViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (message.sent!!) {
            return 2
        }
        return 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder.javaClass == SentMessageViewHolder::class.java) {
            val viewHolder = holder as SentMessageViewHolder
            viewHolder.tvSentMessage.text = message.message
            viewHolder.tvSenderInitial.text = "Me"
            viewHolder.tvSentDt.text = getTimeString(message.sentDt!!)
        }
        else {
            val viewHolder = holder as ReceivedMessageViewHolder
            viewHolder.tvReceivedMessage.text = message.message
            viewHolder.tvReceiverInitial.text = receiverName[0].toString()
            viewHolder.tvReceivedDt.text = getTimeString(message.sentDt!!)
        }
    }

    private fun getTimeString(time: Long): String {
        val dt200011 = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
        val dt = dt200011.plusSeconds(time)

        val today = LocalDateTime.now().toLocalDate()
        if (dt.toLocalDate() == today) {
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            return dt.format(formatter)
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")
        return dt.format(formatter)
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}