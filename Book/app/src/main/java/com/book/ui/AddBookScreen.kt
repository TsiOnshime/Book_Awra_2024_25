package com.code.book.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.code.book.api.RetrofitInstance
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(navController: NavController) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var album by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var songUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Image picker launcher
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // Song picker launcher
    val songPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        songUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add New Book",
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
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cover Image Selection
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color(0xFFB17979), RoundedCornerShape(8.dp))
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Book Cover",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Cover",
                            tint = Color(0xFFB17979),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Add Cover Image",
                            color = Color(0xFFB17979),
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Song Selection
            Button(
                onClick = { songPicker.launch("*/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB17979)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Upload Song",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (songUri != null) "Change Book" else "Select Book",
                        color = Color.White
                    )
                }
            }

            if (songUri != null) {
                Text(
                    text = "Book Selected",
                    color = Color(0xFFB17979),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Book Details Form
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFB17979),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFFB17979),
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color(0xFFB17979)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                label = { Text("Author", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFB17979),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFFB17979),
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color(0xFFB17979)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Genre", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFB17979),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFFB17979),
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color(0xFFB17979)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = album,
                onValueChange = { album = it },
                label = { Text("Category", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFB17979),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFFB17979),
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color(0xFFB17979)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFB17979),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFFB17979),
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = Color(0xFFB17979)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Upload Button
            Button(
                onClick = {
                    if (validateInput(title, artist, genre, album, description, imageUri, songUri)) {
                        isLoading = true
                        scope.launch {
                            try {
                                // Create image file from URI
                                val imageFile = File(context.cacheDir, "temp_image.jpg")
                                context.contentResolver.openInputStream(imageUri!!)?.use { input ->
                                    FileOutputStream(imageFile).use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                // Create song file from URI
                                val songFile = File(context.cacheDir, "temp_song.mp3")
                                context.contentResolver.openInputStream(songUri!!)?.use { input ->
                                    FileOutputStream(songFile).use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                // Create multipart request parts
                                val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
                                val artistPart = artist.toRequestBody("text/plain".toMediaTypeOrNull())
                                val albumPart = album.toRequestBody("text/plain".toMediaTypeOrNull())
                                val genrePart = genre.toRequestBody("text/plain".toMediaTypeOrNull())
                                val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                                val songPart = MultipartBody.Part.createFormData(
                                    "song",
                                    "song.mp3",
                                    songFile.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                                )
                                val imagePart = MultipartBody.Part.createFormData(
                                    "image",
                                    "image.jpg",
                                    imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                )

                                val response = RetrofitInstance.api.uploadBook(
                                    title = titlePart,
                                    artist = artistPart,
                                    album = albumPart,
                                    genre = genrePart,
                                    description = descriptionPart,
                                    song = songPart,
                                    image = imagePart
                                )

                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Book uploaded successfully!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                                // Clean up temporary files
                                File(context.cacheDir, "temp_image.jpg").delete()
                                File(context.cacheDir, "temp_song.mp3").delete()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields and select both image and song", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB17979)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Upload Book", color = Color.White)
                }
            }
        }
    }
}

private fun validateInput(
    title: String,
    artist: String,
    genre: String,
    album: String,
    description: String,
    imageUri: Uri?,
    songUri: Uri?
): Boolean {
    return title.isNotBlank() &&
            artist.isNotBlank() &&
            genre.isNotBlank() &&
            album.isNotBlank() &&
            description.isNotBlank() &&
            imageUri != null &&
            songUri != null
}