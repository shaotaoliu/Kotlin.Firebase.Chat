package com.example.kotlinchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.kotlinchat.adapter.*
import com.example.kotlinchat.model.*
import com.example.kotlinchat.R
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesView: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var ivSend: ImageView
    private lateinit var adapter: MessageAdapter
    private lateinit var messages: ArrayList<Message>
    private var dbRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messagesView = findViewById(R.id.messagesView)
        etMessage = findViewById(R.id.etMessage)
        ivSend = findViewById(R.id.ivSend)

        val receiverName = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")!!
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid!!

        supportActionBar?.title = receiverName

        messages = ArrayList()
        adapter = MessageAdapter(this, messages, receiverName!!)
        messagesView.layoutManager = LinearLayoutManager(this)
        messagesView.adapter = adapter

        ivSend.setOnClickListener {
            sendMessage(senderUid, receiverUid, receiverName!!)
        }

        dbRef.child("chats").child(senderUid + receiverUid).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()

                    for (snap in snapshot.children) {
                        val message = snap.getValue(Message::class.java)
                        messages.add(message!!)
                    }

                    adapter.notifyDataSetChanged()

                    messagesView.post {
                        messagesView.scrollToPosition(messages.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun sendMessage(senderUid: String, receiverUid: String, receiverName: String) {
        val messageText = etMessage.text.toString()
        if (messageText.isBlank()) {
            return
        }

        val dt200011 = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
        val now = dt200011.until(LocalDateTime.now(), ChronoUnit.SECONDS)
        val sendMessage = Message(messageText, true, now)
        val receivedMessage = Message(messageText, false, now)

        dbRef.child("chats").child(senderUid + receiverUid).child("messages").push()
            .setValue(sendMessage).addOnCompleteListener {
                dbRef.child("chats").child(receiverUid + senderUid).child("messages").push()
                    .setValue(receivedMessage)
            }

        etMessage.setText("")
    }
}