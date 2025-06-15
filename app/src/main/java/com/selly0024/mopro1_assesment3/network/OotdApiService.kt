package com.selly0024.mopro1_assesment3.network

import com.selly0024.mopro1_assesment3.model.Ootd
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api-ootd-production-e896.up.railway.app/"


data class GoogleLoginRequest(
    val token: String
)

data class AuthResponse(
    val id: Int,
    val email: String,
    val accessToken: String
)

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OotdApiService {

    @POST("api/auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): AuthResponse

    @GET("api/ootds")
    suspend fun getOotds(): List<Ootd>


    @Multipart
    @POST("api/ootds")
    suspend fun addOotd(
        @Header("Authorization") token: String,
        @Part("namaOutfit") namaOutfit: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part image: MultipartBody.Part
    ): Ootd


    @Multipart
    @PUT("api/ootds/{id}")
    suspend fun updateOotd(
        @Header("Authorization") token: String,
        @Path("id") ootdId: String,
        @Part("namaOutfit") namaOutfit: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part image: MultipartBody.Part?
    ): Ootd


    @DELETE("api/ootds/{id}")
    suspend fun deleteOotd(
        @Header("Authorization") token: String,
        @Path("id") ootdId: String
    ): Response<Unit>
}

object OotdApi {
    val service: OotdApiService by lazy {
        retrofit.create(OotdApiService::class.java)
    }
}