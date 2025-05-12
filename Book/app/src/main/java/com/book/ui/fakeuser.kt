

package com.code.book.data

data class FakeUser(val email: String, val password: String, val role: String)

object FakeUserStore {
    val users = mutableListOf(
        FakeUser("test@example.com", "password123", "writer"),
        FakeUser("reader@example.com", "readpass", "reader")
    )

    fun addUser(email: String, password: String, role: String): Boolean {
        if (users.any { it.email == email }) return false
        users.add(FakeUser(email, password, role))
        return true
    }

    fun authenticate(email: String, password: String): String {
        val user = users.find { it.email == email && it.password == password }
        return user?.role ?: "invalid"
    }
}
