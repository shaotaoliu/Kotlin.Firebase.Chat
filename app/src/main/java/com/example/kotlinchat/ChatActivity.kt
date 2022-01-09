package com.example.kotlinchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesView: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var ivSend: ImageView
    private lateinit var adapter: MessageAdapter
    private lateinit var messages: ArrayList<Message>
    private var dbRef = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messagesView = findViewById(R.id.messagesView)
        etMessage = findViewById(R.id.etMessage)
        ivSend = findViewById(R.id.ivSend)

        val receiverName = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid!!

        supportActionBar?.title = receiverName

        messages = ArrayList()
        adapter = MessageAdapter(this, messages)
        messagesView.layoutManager = LinearLayoutManager(this)
        messagesView.adapter = adapter

        ivSend.setOnClickListener {
            val messageText = etMessage.text.toString()
            val message = Message(messageText, senderUid)

            dbRef.child("chats").child(senderUid + receiverUid).child("messages").push()
                .setValue(message).addOnCompleteListener {
                    dbRef.child("chats").child(receiverUid + senderUid).child("messages").push()
                        .setValue(message)
                }

            etMessage.setText("")
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
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    fun addMessage() {

    }
}