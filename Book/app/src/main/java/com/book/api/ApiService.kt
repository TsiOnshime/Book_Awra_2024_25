package com.code.book.api
//
//import com.code.book.model.Book
//import retrofit2.Response
//import retrofit2.http.Body
//import retrofit2.http.GET
//import retrofit2.http.POST
//
//interface ApiService {
//
//    @GET("songs")
//    suspend fun getBooks(): Response<List<Book>>
//
//    @POST("books")
//    suspend fun addBook(@Body book: Book): Response<Book>
//}






import com.code.book.model.Book
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

data class AuthResponse(
    val accessToken: String
    // Role will be handled separately for now
)

data class LoginRequest(
    val email: String,
    val userPassword: String
)

data class RegisterArtistRequest(
    val email: String,
    val userPassword: String,
    val fullName: String,
    val dateOfBirth: String,
    val gender: String
)

data class RegisterArtistResponse(
    val message: String
)

data class ProfileResponse(
    val _id: String,
    val email: String,
    val artist: String,
    val bio: String,
    val genre: String,
    val description: String,
    val imageData: ImageData,
    val imageContentType: String,
    val uploadDate: String
)

data class ImageData(
    val data: List<Int>,
    val contentType: String
)

interface ApiService {
    @GET("songs")
    suspend fun getBooks(): Response<List<Book>>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: String): Response<Book>

    @POST("books")
    suspend fun createBook(@Body book: Book): Response<Book>

    @PUT("books/{id}")
    suspend fun updateBook(
        @Path("id") id: String,
        @Body book: Book
    ): Response<Book>

    @DELETE("songs/{id}")
    suspend fun deleteBook(@Path("id") id: String): Response<Unit>

    @GET("songs/search/title")
    suspend fun searchBooks(@Query("title") title: String): Response<List<Book>>

    @GET("songs/stream/{id}")
    @Streaming
    suspend fun streamPdf(@Path("id") id: String): Response<ResponseBody>

    @GET("songs/image/{id}")
    suspend fun getSongImage(@Path("id") id: String): Response<ResponseBody>

    @Multipart
    @POST("songs/upload")
    suspend fun uploadBook(
        @Part("title") title: RequestBody,
        @Part("artist") artist: RequestBody,
        @Part("album") album: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("description") description: RequestBody,
        @Part song: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Response<Book>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/register-artist")
    suspend fun registerArtist(@Body request: RegisterArtistRequest): Response<RegisterArtistResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("profile/{email}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<ProfileResponse>

    @Multipart
    @POST("profile/upload")
    suspend fun uploadProfile(
        @Header("Authorization") token: String,
        @Part("artist") artist: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("description") description: RequestBody,
        @Part imageData: MultipartBody.Part,
        @Part("imageContentType") imageContentType: RequestBody
    ): Response<Unit>

    @Multipart
    @PUT("profile/updateprofile/{email}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("email") email: String,
        @Part("artist") artist: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("description") description: RequestBody,
        @Part imageData: MultipartBody.Part,
        @Part("imageContentType") imageContentType: RequestBody
    ): Response<Unit>
}


