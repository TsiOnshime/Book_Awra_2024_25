//package com.code.book.ui
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.code.book.data.FakeUserStore
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//@Composable
//fun LoginScreen(navController: NavController) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val context = LocalContext.current
//    var isLoading by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(16.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 32.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "BookAura",
//                color = Color(0xFFB17979),
//                fontSize = 28.sp,
//                fontFamily = FontFamily.Cursive
//            )
//        }
//
//        // Email Field
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email", color = Color.White) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
//            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color(0xFFB17979),
//                unfocusedBorderColor = Color.Gray,
//                focusedTextColor = Color.White,
//                unfocusedTextColor = Color.White
//            )
//        )
//
//        // Password Field
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password", color = Color.White) },
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
//            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color(0xFFB17979),
//                unfocusedBorderColor = Color.Gray,
//                focusedTextColor = Color.White,
//                unfocusedTextColor = Color.White
//            )
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Login Button
//        Button(
//            onClick = {
//                isLoading = true
//                CoroutineScope(Dispatchers.IO).launch {
//                    val role = FakeUserStore.authenticate(email, password)
//                    launch(Dispatchers.Main) {
//                        isLoading = false
//                        when (role) {
//                            "writer" -> {
//                                Toast.makeText(context, "Login successful! Welcome Writer.", Toast.LENGTH_SHORT).show()
//                                navController.navigate("add_story") {
//                                    popUpTo("login") { inclusive = true }
//                                }
//                            }
//                            "reader" -> {
//                                Toast.makeText(context, "Login successful! Welcome Reader.", Toast.LENGTH_SHORT).show()
//                                navController.navigate("home") {
//                                    popUpTo("login") { inclusive = true }
//                                }
//                            }
//                            else -> Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB17979)),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login", color = Color.White)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Forgot Password Button
//        TextButton(onClick = { navController.navigate("forgot_password") }) {
//            Text("Forgot Password?", color = Color.White)
//        }
//
//        // Sign Up Button
//        TextButton(onClick = { navController.navigate("signup") }) {
//            Text("Don't have an account? Sign Up", color = Color.White)
//        }
//
//        // Loading Indicator
//        if (isLoading) {
//            CircularProgressIndicator()
//        }
//
//        // Add a Spacer with weight to push content to the top and fill the remaining height
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
