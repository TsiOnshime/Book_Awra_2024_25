//package com.code.book.ui
//
//import android.net.Uri
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.code.book.model.Book
//import androidx.compose.ui.layout.ContentScale
//import
//
//@Composable
//fun AddStoryScreen(navController: NavController, books: MutableList<Book>, onBookUploaded: (Book) -> Unit) {
//    val context = LocalContext.current
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//    var description by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? -> imageUri = uri }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Add Story Info",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Book cover image section
//        Box(
//            modifier = Modifier
//                .height(200.dp)
//                .fillMaxWidth()
//                .clickable { launcher.launch("image/*") }
//                .background(Color.LightGray, RoundedCornerShape(12.dp)),
//            contentAlignment = Alignment.Center
//        ) {
//            if (imageUri != null) {
//                Image(
//                    painter = rememberAsyncImagePainter(imageUri),
//                    contentDescription = "Selected Image",
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                Text("Tap to upload cover", color = Color.DarkGray)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Description input section
//        TextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Description") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(120.dp)
//                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
//            textStyle = LocalTextStyle.current.copy(color = Color.White),
//            shape = RoundedCornerShape(12.dp),
//            maxLines = 5,
//            singleLine = false,
//            colors = TextFieldDefaults.colors(
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Button Row for Edit, Clear, and Upload
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            // Clear Button to reset image and description
//            Button(
//                onClick = {
//                    imageUri = null
//                    description = ""
//                    Toast.makeText(context, "Form Cleared", Toast.LENGTH_SHORT).show()
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("🧹 Clear", color = Color.White)
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            // Upload Button with loading state
//            Button(
//                onClick = {
//                    if (imageUri == null || description.isBlank()) {
//                        Toast.makeText(context, "Please add a cover and description", Toast.LENGTH_SHORT).show()
//                    } else {
//                        isLoading = true
//                        Toast.makeText(context, "Uploading story... ⬆️", Toast.LENGTH_SHORT).show()
//
//                        // Create new Book and add to list
//                        val newBook = Book(imageUri.toString(), description)
//                        onBookUploaded(newBook)
//
//                        // Simulate upload delay
//                        android.os.Handler().postDelayed({
//                            isLoading = false
//                            navController.navigate("home") {
//                                popUpTo("add_story") { inclusive = true }
//                            }
//                        }, 2000)
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB17979)),
//                modifier = Modifier.weight(1f)
//            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
//                } else {
//                    Text("⬆️ Upload Story", color = Color.White)
//                }
//            }
//        }
//
//        // Add a Spacer with weight to push content to the top and fill the remaining height
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}
