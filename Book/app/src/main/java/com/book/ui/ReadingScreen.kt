package com.code.book.ui

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(navController: NavController, bookId: String) {
    val context = LocalContext.current
    var currentPage by remember { mutableStateOf(0) }
    var totalPages by remember { mutableStateOf(0) }
    var pdfBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var pdfRenderer: PdfRenderer? = null
    var fileDescriptor: ParcelFileDescriptor? = null

    LaunchedEffect(bookId) {
        try {
            val file = File(context.filesDir, "book_$bookId.pdf")
            if (file.exists()) {
                fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(fileDescriptor!!)
                totalPages = pdfRenderer!!.pageCount

                // Render first page
                pdfRenderer?.let { renderer ->
                    renderer.openPage(currentPage).use { page ->
                        val bitmap = android.graphics.Bitmap.createBitmap(
                            page.width * 3,
                            page.height * 3,
                            android.graphics.Bitmap.Config.ARGB_8888
                        )
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        pdfBitmap = bitmap
                    }
                }
            }
        } catch (e: Exception) {
            // Handle error
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            pdfRenderer?.close()
            fileDescriptor?.close()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BookAura",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color(0xFF8B4513),
                            fontFamily = FontFamily.Cursive
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF8B4513)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5E6D3)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5E6D3))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                pdfBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "PDF Page ${currentPage + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 8.dp)
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE6D5C3)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (currentPage > 0) {
                                    currentPage--
                                    pdfRenderer?.let { renderer ->
                                        renderer.openPage(currentPage).use { page ->
                                            val bitmap = android.graphics.Bitmap.createBitmap(
                                                page.width * 3,
                                                page.height * 3,
                                                android.graphics.Bitmap.Config.ARGB_8888
                                            )
                                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                            pdfBitmap = bitmap
                                        }
                                    }
                                }
                            },
                            enabled = currentPage > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8B4513),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Previous")
                        }

                        Text(
                            text = "Page ${currentPage + 1} of $totalPages",
                            color = Color(0xFF5C4033)
                        )

                        Button(
                            onClick = {
                                if (currentPage < totalPages - 1) {
                                    currentPage++
                                    pdfRenderer?.let { renderer ->
                                        renderer.openPage(currentPage).use { page ->
                                            val bitmap = android.graphics.Bitmap.createBitmap(
                                                page.width * 3,
                                                page.height * 3,
                                                android.graphics.Bitmap.Config.ARGB_8888
                                            )
                                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                            pdfBitmap = bitmap
                                        }
                                    }
                                }
                            },
                            enabled = currentPage < totalPages - 1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8B4513),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
} 