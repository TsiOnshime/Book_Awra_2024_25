package com.code.book.api

data class RegisterRequest(
    val email: String,
    val userPassword: String,
    val fullName: String,
    val dateOfBirth: String,
    val gender: String
) 