package com.example.kotlinchat.model

import java.time.LocalDateTime

data class Message(
    val message: String? = null,
    val senderId: String? = null

//    val sent: Boolean? = null,
//    val sentDt: LocalDateTime? = null
)

//class Message {
//    var message: String? = null
//    var senderId: String? = null
//
//    constructor() {}
//
//    constructor(message: String, senderId: String) {
//        this.message = message
//        this.senderId = senderId
//    }
//}