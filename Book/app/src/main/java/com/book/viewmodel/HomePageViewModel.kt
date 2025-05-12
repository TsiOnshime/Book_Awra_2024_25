package com.code.book.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.book.model.Book
import com.code.book.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomePageUiState {
    object Loading : HomePageUiState()
    data class Success(
        val allBooks: List<Book>,
        val newBooks: List<Book>,
        val yourChoiceBooks: List<Book>,
        val authorsChoiceBooks: List<Book>
    ) : HomePageUiState()
    data class Error(val message: String) : HomePageUiState()
    data class Offline(
        val allBooks: List<Book>,
        val newBooks: List<Book>,
        val yourChoiceBooks: List<Book>,
        val authorsChoiceBooks: List<Book>
    ) : HomePageUiState()
}

class HomePageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<HomePageUiState>(HomePageUiState.Loading)
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    // Store the last successful books fetch
    private var lastSuccessfulBooks: List<Book> = emptyList()

    init {
        fetchBooks()
    }

    private fun processBooks(books: List<Book>): HomePageUiState.Success {
        // Sort books by upload date for "New Books"
        val sortedBooks = books.sortedByDescending { it.uploadDate }
        val newBooks = sortedBooks.take(5) // Latest 5 books
        
        // Get all unique genres from the books
        val allGenres = books.map { it.genre.lowercase() }.distinct()
        
        // Split genres into two groups
        val genreGroups = allGenres.chunked((allGenres.size + 1) / 2)
        
        // Your Choice: First half of genres
        val yourChoiceGenres = genreGroups.firstOrNull() ?: emptyList()
        val yourChoiceBooks = books.filter { book ->
            yourChoiceGenres.contains(book.genre.lowercase())
        }
        
        // Author's Choice: Second half of genres
        val authorsChoiceGenres = genreGroups.lastOrNull() ?: emptyList()
        val authorsChoiceBooks = books.filter { book ->
            authorsChoiceGenres.contains(book.genre.lowercase())
        }
        
        return HomePageUiState.Success(
            allBooks = books,
            newBooks = newBooks,
            yourChoiceBooks = yourChoiceBooks,
            authorsChoiceBooks = authorsChoiceBooks
        )
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.value = HomePageUiState.Loading
            try {
                val response = RetrofitInstance.api.getBooks()
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    
                    if (books.isEmpty()) {
                        _uiState.value = HomePageUiState.Success(
                            allBooks = emptyList(),
                            newBooks = emptyList(),
                            yourChoiceBooks = emptyList(),
                            authorsChoiceBooks = emptyList()
                        )
                        return@launch
                    }
                    
                    // Store successful fetch
                    lastSuccessfulBooks = books
                    _uiState.value = processBooks(books)
                } else {
                    _uiState.value = HomePageUiState.Error(
                        "Failed to fetch books: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("HomePageViewModel", "Error fetching books", e)
                if (e is java.net.UnknownHostException || e is java.net.ConnectException) {
                    // If we have cached books, show them in offline mode
                    if (lastSuccessfulBooks.isNotEmpty()) {
                        _uiState.value = processBooks(lastSuccessfulBooks)
                    } else {
                        _uiState.value = HomePageUiState.Error(
                            "No internet connection and no cached books available"
                        )
                    }
                } else {
                    _uiState.value = HomePageUiState.Error(
                        e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun refreshBooks() {
        fetchBooks()
    }

    fun getBookById(id: String): Book? {
        return when (val state = _uiState.value) {
            is HomePageUiState.Success -> state.allBooks.find { it.id == id }
            is HomePageUiState.Offline -> state.allBooks.find { it.id == id }
            else -> null
        }
    }
}
