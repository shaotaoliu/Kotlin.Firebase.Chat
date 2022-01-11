package com.example.kotlinchat.model

data class Message(
    val message: String? = null,
    val sent: Boolean? = null,
    val sentDt: Long? = null
)