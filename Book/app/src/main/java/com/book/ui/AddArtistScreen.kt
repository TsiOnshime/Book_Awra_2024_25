package com.code.book.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.code.book.api.RetrofitInstance
import com.code.book.api.RegisterArtistRequest
import com.code.book.auth.TokenManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAuthorScreen(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    
    // Check if user is superadmin, if not redirect to home
    LaunchedEffect(Unit) {
        if (!tokenManager.isSuperAdmin()) {
            Toast.makeText(context, "Access denied: Super admin access required", Toast.LENGTH_SHORT).show()
            navController.navigate("home") {
                popUpTo("add_author_screen") { inclusive = true }
            }
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
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

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Add New Author",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Form Fields
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Author's Email", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
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

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Author's Password", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
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

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Author's Full Name", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
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

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth (YYYY-MM-DD)", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
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

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender (male/female)", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF333333),
                unfocusedContainerColor = Color(0xFF333333),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color(0xFFB17979),
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (validateForm(email, password, fullName, dateOfBirth, gender)) {
                    isLoading = true
                    errorMessage = null
                    
                    scope.launch {
                        try {
                            val request = RegisterArtistRequest(
                                email = email,
                                userPassword = password,
                                fullName = fullName,
                                dateOfBirth = dateOfBirth,
                                gender = gender
                            )
                            
                            val response = RetrofitInstance.api.registerArtist(request)
                            
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Author added successfully!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                when (response.code()) {
                                    401 -> errorMessage = "You should be a super admin to add authors"
                                    403 -> errorMessage = "Access denied: You don't have permission to add authors"
                                    else -> errorMessage = "Failed to add author: ${response.code()}"
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = "Please fill all fields correctly"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB17979)
            ),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text("Add Author", color = Color.White)
            }
        }
    }
}

private fun validateForm(
    email: String,
    password: String,
    fullName: String,
    dateOfBirth: String,
    gender: String
): Boolean {
    if (email.isBlank() || password.isBlank() || fullName.isBlank() || 
        dateOfBirth.isBlank() || gender.isBlank()) {
        return false
    }

    // Validate email format
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return false
    }

    // Validate date format (YYYY-MM-DD)
    try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false
        dateFormat.parse(dateOfBirth)
    } catch (e: Exception) {
        return false
    }

    // Validate gender
    if (gender.lowercase() !in listOf("male", "female")) {
        return false
    }

    return true
} 