package com.code.book.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.code.book.api.RetrofitInstance
import com.code.book.model.Book
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    allBooks: List<Book>
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "BookAura",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFFB17979),
                    fontFamily = FontFamily.Cursive
                )
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                if (query.isNotBlank()) {
                    isSearching = true
                    scope.launch {
                        try {
                            // First search in local books
                            val localResults = allBooks.filter { 
                                it.title.contains(query, ignoreCase = true) ||
                                it.artist.contains(query, ignoreCase = true)
                            }
                            
                            // Then search from API
                            val apiService = RetrofitInstance.api
                            val response = apiService.searchBooks(query)
                            if (response.isSuccessful) {
                                val apiResults = response.body() ?: emptyList()
                                // Combine and deduplicate results
                                searchResults = (localResults + apiResults).distinctBy { it.id }
                            } else {
                                searchResults = localResults
                            }
                        } catch (e: Exception) {
                            // If API call fails, just show local results
                            searchResults = allBooks.filter { 
                                it.title.contains(query, ignoreCase = true) ||
                                it.artist.contains(query, ignoreCase = true)
                            }
                        } finally {
                            isSearching = false
                        }
                    }
                } else {
                    searchResults = emptyList()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search books...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFFB17979)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF333333),
                unfocusedContainerColor = Color(0xFF333333),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color(0xFFB17979),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results
        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFB17979))
            }
        } else if (searchQuery.isBlank()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Search for books by title or author",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No books found",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { book ->
                    BookSearchItem(
                        book = book,
                        onClick = {
                            navController.navigate("description/${book.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookSearchItem(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "By ${book.artist}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
} 