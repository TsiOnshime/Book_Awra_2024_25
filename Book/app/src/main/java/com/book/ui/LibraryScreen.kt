package com.code.book.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.code.book.model.Book
import com.code.book.data.LibraryStore
import com.code.book.viewmodel.HomePageViewModel
import com.code.book.viewmodel.HomePageUiState
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(navController: NavController) {
    val context = LocalContext.current
    val libraryStore = remember { LibraryStore(context) }
    val homePageViewModel: HomePageViewModel = viewModel()
    val uiState by homePageViewModel.uiState.collectAsState()
    var bookmarkedIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var bookmarkedBooks by remember { mutableStateOf<List<Book>>(emptyList()) }
    val scope = rememberCoroutineScope()

    // Refresh books when entering the screen
    LaunchedEffect(Unit) {
        homePageViewModel.refreshBooks()
    }

    // Collect bookmarked books
    LaunchedEffect(Unit) {
        libraryStore.bookmarkedBooks.collect { ids ->
            bookmarkedIds = ids
            // Get book details from HomePageViewModel based on current state
            bookmarkedBooks = when (val state = uiState) {
                is HomePageUiState.Success -> {
                    ids.mapNotNull { id -> state.allBooks.find { it.id == id } }
                }
                is HomePageUiState.Offline -> {
                    ids.mapNotNull { id -> state.allBooks.find { it.id == id } }
                }
                else -> emptyList()
            }
        }
    }

    // Refresh bookmarked books when UI state changes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is HomePageUiState.Success -> {
                bookmarkedBooks = bookmarkedIds.mapNotNull { id -> 
                    state.allBooks.find { it.id == id } 
                }
            }
            is HomePageUiState.Offline -> {
                bookmarkedBooks = bookmarkedIds.mapNotNull { id -> 
                    state.allBooks.find { it.id == id } 
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFB17979)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "BookAura",
                color = Color(0xFFB17979),
                fontSize = 28.sp,
                fontFamily = FontFamily.Cursive
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bookmarked Books",
            color = Color(0xFFB17979),
            fontSize = 32.sp,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is HomePageUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFB17979))
                }
            }
            is HomePageUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as HomePageUiState.Error).message,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
            else -> {
                if (bookmarkedBooks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No bookmarked books yet...",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(bookmarkedBooks) { book ->
                            BookRow(book = book) {
                                navController.navigate("description/${book.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookRow(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(book.imageUrl),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(
                    text = book.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "by ${book.artist}",
                    color = Color(0xFFB17979),
                    fontSize = 16.sp
                )
            }
        }
    }
}