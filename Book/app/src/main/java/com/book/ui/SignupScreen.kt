package com.code.book.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.code.book.api.RetrofitInstance
import com.code.book.api.RegisterRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            color = Color(0xFFB17979),
            fontSize = 28.sp,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = Color.White) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password", tint = Color.White) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name Field
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Full Name", tint = Color.White) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date of Birth Field
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of Birth (YYYY-MM-DD)", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Date of Birth", tint = Color.White) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gender Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "male",
                    onClick = { gender = "male" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFB17979),
                        unselectedColor = Color.Gray
                    )
                )
                Text(text = "Male", color = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = gender == "female",
                    onClick = { gender = "female" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFB17979),
                        unselectedColor = Color.Gray
                    )
                )
                Text(text = "Female", color = Color.White)
            }
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

        // Sign Up Button
        Button(
            onClick = {
                if (validateForm(email, password, fullName, dateOfBirth, gender)) {
                    isLoading = true
                    errorMessage = null
                    
                    scope.launch {
                        try {
                            val request = RegisterRequest(
                                email = email,
                                userPassword = password,
                                fullName = fullName,
                                dateOfBirth = dateOfBirth,
                                gender = gender
                            )
                            
                            val response = RetrofitInstance.api.register(request)
                            
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate("login") {
                                    popUpTo("signup") { inclusive = true }
                                }
                            } else {
                                when (response.code()) {
                                    409 -> errorMessage = "Email already exists"
                                    else -> errorMessage = "Registration failed: ${response.code()}"
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB17979)),
            enabled = !isLoading
        ) {
            Text(
                text = if (isLoading) "Signing up..." else "Sign Up",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Link
        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text(
                text = "Already have an account? Login",
                color = Color.White
            )
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
    if (email.isBlank() || password.isBlank() || fullName.isBlank() || dateOfBirth.isBlank() || gender.isBlank()) {
        return false
    }

    // Validate email format
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
    if (!email.matches(emailRegex)) {
        return false
    }

    // Validate date format (YYYY-MM-DD)
    val dateRegex = "\\d{4}-\\d{2}-\\d{2}".toRegex()
    if (!dateOfBirth.matches(dateRegex)) {
        return false
    }

    return true
} 