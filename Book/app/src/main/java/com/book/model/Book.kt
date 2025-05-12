package com.code.book.model

import java.text.SimpleDateFormat
import java.util.*

//data class Book(
//    val id: String,
//    val title: String,
//    val author: String,
//    val genre: String,
//    val description: String,
//    val pages: Int,
//    val releaseDate: String,
//    val imageUrl: String
//)
//


data class Book(
    val _id: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val description: String,
    val songContentType: String,
    val imageContentType: String,
    val uploadDate: String
) {
    val id: String get() = _id
    
    // Use the correct endpoint for images
    val imageUrl: String get() = "http://192.168.1.3:3006/songs/$id"
    
    // Format the upload date to a more readable format
    val formattedDate: String
        get() {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(uploadDate)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                uploadDate
            }
        }

    // Get a default image if the actual image is not available
    val defaultImageUrl: String
        get() = "https://via.placeholder.com/150?text=No+Image"

    // Get a formatted genre
    val formattedGenre: String
        get() = genre.capitalize(Locale.getDefault())

    // Get a formatted artist name
    val formattedArtist: String
        get() = artist.capitalize(Locale.getDefault())

    // Get a formatted title
    val formattedTitle: String
        get() = title.capitalize(Locale.getDefault())

    // Get a formatted description
    val formattedDescription: String
        get() = description.capitalize(Locale.getDefault())

    // Get a formatted album name
    val formattedAlbum: String
        get() = album.capitalize(Locale.getDefault())
}

