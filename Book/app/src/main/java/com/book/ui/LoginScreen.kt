package com.code.book.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.code.book.api.LoginRequest
import com.code.book.api.RetrofitInstance
import com.code.book.auth.TokenManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BookAura",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFFB17979),
                fontFamily = FontFamily.Cursive
            ),
            modifier = Modifier.padding(bottom = 48.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = Color(0xFFB17979)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFB17979),
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color(0xFFB17979)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.White) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password",
                    tint = Color(0xFFB17979)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFB17979),
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color(0xFFB17979)
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    isLoading = true
                    scope.launch {
                        try {
                            val response = RetrofitInstance.api.login(
                                LoginRequest(email, userPassword = password)
                            )
                            if (response.isSuccessful) {
                                response.body()?.let { authResponse ->
                                    android.util.Log.d("LoginScreen", "Login response: $authResponse")
                                    tokenManager.saveToken(authResponse.accessToken)
                                    onLoginSuccess()
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Login failed: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
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
                Text("Login", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Link
        TextButton(
            onClick = { navController.navigate("signup") }
        ) {
            Text(
                text = "Don't have an account? Sign Up",
                color = Color.White
            )
        }
    }
} 