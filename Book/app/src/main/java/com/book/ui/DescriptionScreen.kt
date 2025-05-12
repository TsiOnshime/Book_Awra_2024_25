package com.code.book.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.code.book.model.Book
import com.code.book.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import com.code.book.data.LibraryStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionScreen(
    navController: NavController,
    book: Book
) {
    val context = LocalContext.current
    val libraryStore = remember { LibraryStore(context) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var imageFile by remember { mutableStateOf<File?>(null) }
    val scope = rememberCoroutineScope()
    var isBookSaved by remember { mutableStateOf(false) }

    // Load image from local storage
    LaunchedEffect(book.id) {
        val file = File(context.filesDir, "book_${book.id}_image.jpg")
        if (file.exists()) {
            imageFile = file
        } else {
            // If image doesn't exist locally, fetch it
            try {
                val response = RetrofitInstance.api.getSongImage(book.id)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        responseBody.byteStream().use { inputStream ->
                            FileOutputStream(file).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        imageFile = file
                    }
                }
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    // Check if book is saved when screen is displayed
    LaunchedEffect(book.id) {
        try {
            isBookSaved = libraryStore.isBookSaved(book.id)
        } catch (e: Exception) {
            // Handle any errors silently
            isBookSaved = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BookAura",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color(0xFFB17979),
                            fontFamily = FontFamily.Cursive
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFB17979)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                ),
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                try {
                                    if (isBookSaved) {
                                        libraryStore.removeBook(book.id)
                                        isBookSaved = false
                                    } else {
                                        libraryStore.addBook(book.id)
                                        isBookSaved = true
                                    }
                                } catch (e: Exception) {
                                    // Handle any errors silently
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookSaved) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isBookSaved) "Remove from Library" else "Add to Library",
                            tint = Color(0xFFB17979)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Book Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFF8B5A5A))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageFile != null) {
                    AsyncImage(
                        model = imageFile,
                        contentDescription = "Book Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = Color.White
                    )
                }
            }

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            try {
                                // Check if PDF already exists in local storage
                                val pdfFile = File(context.filesDir, "book_${book.id}.pdf")
                                if (pdfFile.exists()) {
                                    // PDF already exists, navigate directly to reading screen
                                    navController.navigate("reading_screen/${book.id}")
                                    isLoading = false
                                    return@launch
                                }

                                // If PDF doesn't exist, fetch it
                                val apiService = RetrofitInstance.api
                                val response = apiService.streamPdf(book.id)
                                if (response.isSuccessful) {
                                    val responseBody = response.body()
                                    if (responseBody != null) {
                                        withContext(Dispatchers.IO) {
                                            // Save PDF to local storage
                                            responseBody.byteStream().use { inputStream ->
                                                FileOutputStream(pdfFile).use { outputStream ->
                                                    inputStream.copyTo(outputStream)
                                                }
                                            }
                                        }
                                        // Navigate to ReadingScreen with book ID
                                        navController.navigate("reading_screen/${book.id}")
                                    } else {
                                        errorMessage = "PDF data is empty"
                                    }
                                } else {
                                    errorMessage = "Failed to download PDF: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error loading PDF: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Start Reading")
                    }
                }

                Button(
                    onClick = {
                        navController.navigate("library_screen")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Library")
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Book Details
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFFB17979),
                    fontFamily = FontFamily.Cursive
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            BookDetailItem("Author", book.artist)
            BookDetailItem("Genre", book.genre)
            BookDetailItem("Book Name", book.album)
            BookDetailItem("Uploaded", book.formattedDate)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Add a Spacer with weight to push content to the top and fill the remaining height
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun BookDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White
            )
        )
    }
}

