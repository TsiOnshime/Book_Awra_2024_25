package com.code.book.ui

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import coil.compose.rememberAsyncImagePainter
import com.code.book.api.RetrofitInstance
import com.code.book.auth.TokenManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()
    val userRole = tokenManager.getUserRole()
    val isCreator = userRole == "artist" || userRole == "author"
    val userEmail = tokenManager.getUserEmail() ?: ""
    
    var fullName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isInitialLoad by remember { mutableStateOf(true) }

    // Fetch existing profile data
    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty() && isCreator) {
            try {
                Log.d("EditProfileScreen", "Fetching profile for email: $userEmail")
                val response = RetrofitInstance.api.getProfile(
                    token = "Bearer ${tokenManager.getToken()}",
                    email = userEmail
                )
                
                if (response.isSuccessful) {
                    response.body()?.let { profile ->
                        Log.d("EditProfileScreen", "Profile fetched successfully: $profile")
                        fullName = profile.artist
                        bio = profile.bio
                        genre = profile.genre
                        description = profile.description
                        
                        // Handle profile image
                        if (profile.imageData.data.isNotEmpty()) {
                            try {
                                Log.d("EditProfileScreen", "Processing image data, length: ${profile.imageData.data.size}")
                                // Convert List<Int> to ByteArray
                                val imageBytes = profile.imageData.data.map { it.toByte() }.toByteArray()
                                Log.d("EditProfileScreen", "Image converted to byte array, size: ${imageBytes.size}")
                                
                                val file = File(context.cacheDir, "profile_image.jpg")
                                FileOutputStream(file).use { outputStream ->
                                    outputStream.write(imageBytes)
                                }
                                Log.d("EditProfileScreen", "Image saved to file: ${file.absolutePath}")
                                
                                imageUri = Uri.fromFile(file)
                                Log.d("EditProfileScreen", "Image URI set: $imageUri")
                            } catch (e: Exception) {
                                Log.e("EditProfileScreen", "Error loading profile image", e)
                                errorMessage = "Error loading profile image: ${e.message}"
                            }
                        } else {
                            Log.d("EditProfileScreen", "No image data found in profile")
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("EditProfileScreen", "Failed to fetch profile. Status: ${response.code()}, Error: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("EditProfileScreen", "Error fetching profile", e)
            } finally {
                isInitialLoad = false
            }
        } else {
            Log.d("EditProfileScreen", "Skipping profile fetch. Email: $userEmail, isCreator: $isCreator")
            isInitialLoad = false
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isInitialLoad) {
            CircularProgressIndicator(
                color = Color(0xFFB17979),
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Text(
                text = "Edit Profile",
                color = Color(0xFFB17979),
                fontSize = 28.sp,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onError = { Log.e("EditProfileScreen", "Error loading image: ${it.result.throwable}") },
                        onSuccess = { Log.d("EditProfileScreen", "Image loaded successfully") }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Add Profile Image",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(if (userRole == "author") "Author Name" else "Artist Name", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB17979),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Creator-specific fields (for both artists and authors)
            if (isCreator) {
                // Bio Field
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Bio", tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB17979),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Genre Field
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.List, contentDescription = "Genre", tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB17979),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Description", tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB17979),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Error Message
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Save Button
            Button(
                onClick = {
                    if (isCreator && imageUri == null) {
                        errorMessage = "Please select a profile image"
                        return@Button
                    }
                    
                    if (userEmail.isBlank()) {
                        errorMessage = "User email not found"
                        return@Button
                    }
                    
                    isLoading = true
                    errorMessage = null
                    
                    scope.launch {
                        try {
                            if (isCreator) {
                                // Convert Uri to File
                                val inputStream = context.contentResolver.openInputStream(imageUri!!)
                                val file = File(context.cacheDir, "profile_image.jpg")
                                FileOutputStream(file).use { outputStream ->
                                    inputStream?.copyTo(outputStream)
                                }

                                // Create multipart request
                                val artistPart = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
                                val bioPart = bio.toRequestBody("text/plain".toMediaTypeOrNull())
                                val genrePart = genre.toRequestBody("text/plain".toMediaTypeOrNull())
                                val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                                val imagePart = MultipartBody.Part.createFormData(
                                    "imageData",
                                    file.name,
                                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                )
                                val imageContentTypePart = "image/jpeg".toRequestBody("text/plain".toMediaTypeOrNull())

                                // Since we already fetched the profile, we know it exists, so use update API
                                val response = RetrofitInstance.api.updateProfile(
                                    token = "Bearer ${tokenManager.getToken()}",
                                    email = userEmail,
                                    artist = artistPart,
                                    bio = bioPart,
                                    genre = genrePart,
                                    description = descriptionPart,
                                    imageData = imagePart,
                                    imageContentType = imageContentTypePart
                                )

                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.e("EditProfileScreen", "Failed to update profile: $errorBody")
                                    errorMessage = "Failed to update profile: $errorBody"
                                }
                            } else {
                                // Handle regular user profile update
                                // Add your regular user profile update logic here
                            }
                        } catch (e: Exception) {
                            Log.e("EditProfileScreen", "Error saving profile", e)
                            errorMessage = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB17979)),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "Saving..." else "Save Changes",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = {
                    tokenManager.clearToken()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(
                    text = "Logout",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
