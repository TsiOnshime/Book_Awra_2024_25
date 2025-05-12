package com.code.book.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.code.book.R
import com.code.book.model.Book
import com.code.book.api.RetrofitInstance
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun HomePage(
    navController: NavController,
    newBooks: List<Book>,
    yourChoiceBooks: List<Book>,
    authorsChoiceBooks: List<Book>,
    modifier: Modifier = Modifier,
    onBookUploaded: (Book) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Fetch images for all books
    LaunchedEffect(newBooks, yourChoiceBooks, authorsChoiceBooks) {
        val allBooks = newBooks + yourChoiceBooks + authorsChoiceBooks
        allBooks.forEach { book ->
            scope.launch {
                try {
                    val response = RetrofitInstance.api.getSongImage(book.id)
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val imageFile = File(context.filesDir, "book_${book.id}_image.jpg")
                            responseBody.byteStream().use { inputStream ->
                                FileOutputStream(imageFile).use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Handle error silently
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        // Top Row: BookAura and User Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BookAura",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFFB17979),
                    fontFamily = FontFamily.Cursive
                )
            )
            IconButton(
                onClick = { navController.navigate("profile_screen") }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Check if all book lists are empty
        if (newBooks.isEmpty() && yourChoiceBooks.isEmpty() && authorsChoiceBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Books Available",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please check your internet connection or try again later",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Book Sections
            BookSection(
                title = "New Books",
                books = newBooks,
                onBookClick = { book ->
                    navController.navigate("description/${book.id}")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            BookSection(
                title = "Your Choice",
                books = yourChoiceBooks,
                onBookClick = { book ->
                    navController.navigate("description/${book.id}")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            BookSection(
                title = "Author's Choice",
                books = authorsChoiceBooks,
                onBookClick = { book ->
                    navController.navigate("description/${book.id}")
                }
            )
        }
    }
}

@Composable
private fun BookSection(
    title: String,
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(books) { book ->
                BookCard(
                    book = book,
                    onClick = { onBookClick(book) }
                )
            }
        }
    }
}

@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var imageFile by remember { mutableStateOf<File?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(book.id) {
        val file = File(context.filesDir, "book_${book.id}_image.jpg")
        if (file.exists()) {
            imageFile = file
        }
    }

    Card(
        modifier = Modifier
            .width(110.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB17979)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF8B5A5A)),
                contentAlignment = Alignment.Center
            ) {
                if (imageFile != null) {
                    AsyncImage(
                        model = imageFile,
                        contentDescription = "Book Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.FillBounds,
                        error = painterResource(id = R.drawable.placeholder_image)
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = book.formattedTitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        fontSize = 12.sp
                    ),
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = book.formattedArtist,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    ),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
        }
    }
}