package com.code.book.ui



import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailSubmitted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            text = "Forgot Password",
            color = Color(0xFFB17979),
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.White) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB17979),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        // Submit Button
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // Simulate sending the email for password reset
                isEmailSubmitted = true
                Toast.makeText(context, "If this email is registered, a reset link will be sent.", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFB17979)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit", color = Color.White)
        }

        // If the email has been submitted, show a success message
        if (isEmailSubmitted) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Check your email for reset instructions", color = Color.White)
        }

        // Back to Login Button
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                navController.popBackStack() // Go back to the login screen
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Back to Login", color = Color.White)
        }
        // Bottom Navigation
        BottomNavigationBar(navController = navController)

        // Add a Spacer with weight to push content to the top and fill the remaining height
        Spacer(modifier = Modifier.weight(1f))
    }
}
