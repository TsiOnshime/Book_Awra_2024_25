package com.code.book.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "library")

class LibraryStore(private val context: Context) {
    private val bookmarkedBooksKey = stringSetPreferencesKey("bookmarked_books")

    val bookmarkedBooks: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[bookmarkedBooksKey] ?: emptySet()
    }

    suspend fun addBook(bookId: String) {
        context.dataStore.edit { preferences ->
            val currentBooks = preferences[bookmarkedBooksKey]?.toMutableSet() ?: mutableSetOf()
            currentBooks.add(bookId)
            preferences[bookmarkedBooksKey] = currentBooks
        }
    }

    suspend fun removeBook(bookId: String) {
        context.dataStore.edit { preferences ->
            val currentBooks = preferences[bookmarkedBooksKey]?.toMutableSet() ?: mutableSetOf()
            currentBooks.remove(bookId)
            preferences[bookmarkedBooksKey] = currentBooks
        }
    }

    suspend fun isBookSaved(bookId: String): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[bookmarkedBooksKey]?.contains(bookId) ?: false
        }.first()
    }

    // Add a function to get all bookmarked books at once
    suspend fun getAllBookmarkedBooks(): Set<String> {
        return context.dataStore.data.map { preferences ->
            preferences[bookmarkedBooksKey] ?: emptySet()
        }.first()
    }
}