package com.code.book.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.code.book.model.Book
import com.code.book.auth.TokenManager

@Composable
fun BottomNavigationBar(navController: NavController, book: Book? = null) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val isSuperAdmin = tokenManager.isSuperAdmin()
    val userRole = tokenManager.getUserRole()
    val canAddBook = isSuperAdmin || userRole == "artist"

    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color(0xFFB17979)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = false,
            onClick = { navController.navigate("search_screen") }
        )
        if (isSuperAdmin) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "Add Author") },
                label = { Text("Add Author") },
                selected = false,
                onClick = { navController.navigate("add_author_screen") }
            )
        }
        if (canAddBook) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Book") },
                label = { Text("Add Book") },
                selected = false,
                onClick = { navController.navigate("add_book_screen") }
            )
        }
    }
}
