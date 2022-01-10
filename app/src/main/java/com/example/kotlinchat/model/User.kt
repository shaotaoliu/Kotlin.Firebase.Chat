package com.example.kotlinchat.model

data class User(
    val name: String? = null,
    val email: String? = null,
    val uid: String? = null
)

//class User {
//    var uid: String? = null
//    var name: String? = null
//    var email: String? = null
//
//    constructor() {
//
//    }
//
//    constructor(name: String?, email: String?, uid: String?) {
//        this.uid = uid
//        this.name = name
//        this.email = email
//    }
//}