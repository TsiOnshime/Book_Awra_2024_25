package com.code.book

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.code.book.ui.*
import com.code.book.viewmodel.HomePageViewModel
import com.code.book.viewmodel.HomePageUiState
import com.code.book.auth.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = Color.Black
                ) {
                    val navController = rememberNavController()
                    val homePageViewModel: HomePageViewModel = viewModel()
                    val uiState by homePageViewModel.uiState.collectAsState()
                    val tokenManager = TokenManager(this)
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Black,
                        bottomBar = {
                            if (currentRoute != "login" && currentRoute != "signup") {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = if (tokenManager.isLoggedIn()) "home" else "login",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("login") {
                                LoginScreen(
                                  navController = navController,
                                    onLoginSuccess = {
                                        homePageViewModel.refreshBooks()
                                    }
                                )
                            }

                            composable("home") {
                                when (uiState) {
                                    is HomePageUiState.Loading -> {
                                        LoadingScreen()
                                    }
                                    is HomePageUiState.Success -> {
                                        val state = uiState as HomePageUiState.Success
                                        HomePage(
                                            navController = navController,
                                            newBooks = state.newBooks,
                                            yourChoiceBooks = state.yourChoiceBooks,
                                            authorsChoiceBooks = state.authorsChoiceBooks,
                                            onBookUploaded = { book ->
                                                homePageViewModel.refreshBooks()
                                            }
                                        )
                                    }
                                    is HomePageUiState.Offline -> {
                                        val state = uiState as HomePageUiState.Offline
                                        HomePage(
                                            navController = navController,
                                            newBooks = state.newBooks,
                                            yourChoiceBooks = state.yourChoiceBooks,
                                            authorsChoiceBooks = state.authorsChoiceBooks,
                                            onBookUploaded = { book ->
                                                homePageViewModel.refreshBooks()
                                            }
                                        )
                                    }
                                    is HomePageUiState.Error -> {
                                        val errorMessage = (uiState as HomePageUiState.Error).message
                                        ErrorScreen(
                                            message = errorMessage,
                                            onRetry = { homePageViewModel.refreshBooks() }
                                        )
                                    }
                                }
                            }
                            composable("library_screen") {
                                LibraryScreen(navController = navController)
                            }
                            composable("search_screen") {
                                when (uiState) {
                                    is HomePageUiState.Success -> {
                                        val state = uiState as HomePageUiState.Success
                                        SearchScreen(
                                            navController = navController,
                                            allBooks = state.allBooks
                                        )
                                    }
                                    is HomePageUiState.Offline -> {
                                        val state = uiState as HomePageUiState.Offline
                                        SearchScreen(
                                            navController = navController,
                                            allBooks = state.allBooks
                                        )
                                    }
                                    is HomePageUiState.Loading -> {
                                        LoadingScreen()
                                    }
                                    is HomePageUiState.Error -> {
                                        val errorMessage = (uiState as HomePageUiState.Error).message
                                        ErrorScreen(
                                            message = errorMessage,
                                            onRetry = { homePageViewModel.refreshBooks() }
                                        )
                                    }
                                }
                            }
                            composable("profile_screen") {
                                ProfileScreen(navController = navController)
                            }
                            composable("edit_profile_screen") {
                                EditProfileScreen(navController = navController)
                            }
                            composable("description/{bookId}") { backStackEntry ->
                                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                                when (uiState) {
                                    is HomePageUiState.Success -> {
                                        val state = uiState as HomePageUiState.Success
                                        val book = state.allBooks.find { it.id == bookId }
                                        if (book != null) {
                                            DescriptionScreen(navController = navController, book = book)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    }
                                    is HomePageUiState.Offline -> {
                                        val state = uiState as HomePageUiState.Offline
                                        val book = state.allBooks.find { it.id == bookId }
                                        if (book != null) {
                                            DescriptionScreen(navController = navController, book = book)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    }
                                    is HomePageUiState.Loading -> {
                                        LoadingScreen()
                                    }
                                    is HomePageUiState.Error -> {
                                        val errorMessage = (uiState as HomePageUiState.Error).message
                                        ErrorScreen(
                                            message = errorMessage,
                                            onRetry = { homePageViewModel.refreshBooks() }
                                        )
                                    }
                                }
                            }

                            composable("reading_screen/{bookId}") { backStackEntry ->
                                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                                ReadingScreen(
                                    navController = navController,
                                    bookId = bookId
                                )
                            }
                            composable("add_book_screen") {
                                AddBookScreen(navController = navController)
                            }
                            composable("add_author_screen") {
                                AddAuthorScreen(navController = navController)
                            }
                            composable("signup") {
                                SignupScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    // Implement your loading screen UI
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    // Implement your error screen UI
}
