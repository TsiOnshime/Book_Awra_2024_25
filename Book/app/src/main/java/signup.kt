//package com.code.book.ui
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
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
//fun SignUpScreen(navController: NavController) {
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var birthdate by remember { mutableStateOf("") }
//    var selectedRole by remember { mutableStateOf("reader") }
//    val roleOptions = listOf("reader", "writer")
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Create Account",
//            color = Color.White,
//            fontSize = 28.sp,
//            fontFamily = FontFamily.Cursive,
//            modifier = Modifier.padding(bottom = 32.dp)
//        )
//
//        // Username Field
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username", color = Color.White) },
//            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Username", tint = Color.White) },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color(0xFFB17979),
//                unfocusedBorderColor = Color.Gray,
//                focusedTextColor = Color.White,
//                unfocusedTextColor = Color.White
//            )
//        )
//
//        // Email Field
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email", color = Color.White) },
//            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email", tint = Color.White) },
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
//            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password", tint = Color.White) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
//            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color(0xFFB17979),
//                unfocusedBorderColor = Color.Gray,
//                focusedTextColor = Color.White,
//                unfocusedTextColor = Color.White
//            )
//        )
//
//        // Birthdate Field
//        OutlinedTextField(
//            value = birthdate,
//            onValueChange = { birthdate = it },
//            label = { Text("Birthdate", color = Color.White) },
//            leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = "Birthdate", tint = Color.White) },
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color(0xFFB17979),
//                unfocusedBorderColor = Color.Gray,
//                focusedTextColor = Color.White,
//                unfocusedTextColor = Color.White
//            )
//        )
//
//        // Role Selection
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//            roleOptions.forEach { role ->
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RadioButton(
//                        selected = selectedRole == role,
//                        onClick = { selectedRole = role },
//                        colors = RadioButtonDefaults.colors(
//                            selectedColor = Color(0xFFB17979),
//                            unselectedColor = Color.Gray
//                        )
//                    )
//                    Text(text = role.capitalize(), color = Color.White)
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Sign Up Button
//        Button(
//            onClick = {
//                CoroutineScope(Dispatchers.IO).launch {
//                    val success = FakeUserStore.addUser(email, password, selectedRole)
//                    launch(Dispatchers.Main) {
//                        if (success) {
//                            Toast.makeText(context, "Sign Up successful!", Toast.LENGTH_SHORT).show()
//                            navController.navigate("login")
//                        } else {
//                            Toast.makeText(context, "User already exists!", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB17979)),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Sign Up", color = Color.White)
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Login Button
//        TextButton(onClick = { navController.navigate("login") }) {
//            Text("Already have an account? Login", color = Color.White)
//        }
//
//        // Add a Spacer with weight to push content to the top and fill the remaining height
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
