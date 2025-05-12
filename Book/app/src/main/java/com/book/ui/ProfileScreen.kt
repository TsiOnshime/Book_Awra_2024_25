package com.code.book.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.AsyncImage
import com.code.book.api.RetrofitInstance
import com.code.book.auth.TokenManager
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val userRole = tokenManager.getUserRole()
    val isCreator = userRole == "artist" || userRole == "author"
    val userEmail = tokenManager.getUserEmail() ?: ""
    
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch profile data
    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty() && isCreator) {
            try {
                Log.d("ProfileScreen", "Fetching profile for email: $userEmail")
                Log.d("ProfileScreen", "User role: $userRole")
                Log.d("ProfileScreen", "Token: ${tokenManager.getToken()}")
                
                val response = RetrofitInstance.api.getProfile(
                    token = "Bearer ${tokenManager.getToken()}",
                    email = userEmail
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { profile ->
                        Log.d("ProfileScreen", "Profile fetched successfully: $profile")
                        fullName = profile.artist
                        bio = profile.bio
                        genre = profile.genre
                        description = profile.description
                        
                        // Handle profile image
                        if (profile.imageData.data.isNotEmpty()) {
                            try {
                                Log.d("ProfileScreen", "Processing image data, length: ${profile.imageData.data.size}")
                                // Convert List<Int> to ByteArray
                                val imageBytes = profile.imageData.data.map { it.toByte() }.toByteArray()
                                Log.d("ProfileScreen", "Image converted to byte array, size: ${imageBytes.size}")
                                
                                val file = File(context.cacheDir, "profile_image.jpg")
                                FileOutputStream(file).use { outputStream ->
                                    outputStream.write(imageBytes)
                                }
                                Log.d("ProfileScreen", "Image saved to file: ${file.absolutePath}")
                                
                                imageUri = android.net.Uri.fromFile(file)
                                Log.d("ProfileScreen", "Image URI set: $imageUri")
                            } catch (e: Exception) {
                                Log.e("ProfileScreen", "Error loading profile image", e)
                                errorMessage = "Error loading profile image: ${e.message}"
                            }
                        } else {
                            Log.d("ProfileScreen", "No image data found in profile")
                        }
                    } ?: run {
                        Log.e("ProfileScreen", "Response body is null")
                        errorMessage = "Profile data is empty"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ProfileScreen", "Failed to fetch profile. Status: ${response.code()}, Error: $errorBody")
                    when (response.code()) {
                        401 -> errorMessage = "Unauthorized. Please login again."
                        404 -> errorMessage = "Profile not found. Please create a profile."
                        else -> errorMessage = "Failed to load profile: ${errorBody ?: "Unknown error"}"
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Error fetching profile", e)
                errorMessage = "Error loading profile: ${e.message}"
            } finally {
                isLoading = false
            }
        } else {
            Log.d("ProfileScreen", "Skipping profile fetch. Email: $userEmail, isCreator: $isCreator")
            if (userEmail.isEmpty()) {
                errorMessage = "User email not found"
            } else if (!isCreator) {
                errorMessage = "Profile is only available for artists and authors, your are admin"
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFFB17979),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Header with Edit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    color = Color(0xFFB17979),
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Cursive
                )
                Row {
                    if (isCreator) {
                        IconButton(
                            onClick = { navController.navigate("edit_profile_screen") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color(0xFFB17979)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            tokenManager.clearToken()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color(0xFFB17979)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Image",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Details
            if (isCreator) {
                ProfileDetailItem(
                    icon = Icons.Default.Person,
                    label = if (userRole == "author") "Author Name" else "Artist Name",
                    value = fullName
                )

                ProfileDetailItem(
                    icon = Icons.Default.Info,
                    label = "Bio",
                    value = bio
                )

                ProfileDetailItem(
                    icon = Icons.Default.List,
                    label = "Genre",
                    value = genre
                )

                ProfileDetailItem(
                    icon = Icons.Default.Info,
                    label = "Description",
                    value = description
                )
            } else {
                Text(
                    text = "Regular User",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Error Message
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Navigation

        }
    }
}

@Composable
private fun ProfileDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFFB17979),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = Color(0xFFB17979),
                fontSize = 16.sp
            )
        }
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 28.dp)
        )
    }
} 