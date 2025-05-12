package com.code.book.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.book.api.RetrofitInstance
import com.code.book.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookUiState {
    object Loading : BookUiState()
    data class Success(val books: List<Book>) : BookUiState()
    data class Error(val message: String) : BookUiState()
}

class BookViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        fetchBooks()
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _uiState.value = BookUiState.Loading
            try {
                val response = RetrofitInstance.api.getBooks()
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    _uiState.value = BookUiState.Success(books)
                } else {
                    _uiState.value = BookUiState.Error(
                        "Failed to fetch books: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("BookViewModel", "Error fetching books", e)
                _uiState.value = BookUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun refreshBooks() {
        fetchBooks()
    }

    fun getBookById(id: String): Book? {
        return when (val state = _uiState.value) {
            is BookUiState.Success -> state.books.find { it.id == id }
            else -> null
        }
    }
}
