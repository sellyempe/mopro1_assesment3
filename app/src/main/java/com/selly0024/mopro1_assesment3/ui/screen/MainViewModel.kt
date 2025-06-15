package com.selly0024.mopro1_assesment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selly0024.mopro1_assesment3.model.Ootd
import com.selly0024.mopro1_assesment3.network.AuthResponse
import com.selly0024.mopro1_assesment3.network.OotdApi
import com.selly0024.mopro1_assesment3.network.GoogleLoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

enum class ApiStatus { LOADING, SUCCESS, FAILED }

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Ootd>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun loginWithGoogle(googleIdToken: String, onLoginSuccess: (AuthResponse) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = GoogleLoginRequest(token = googleIdToken)
                val response = OotdApi.service.loginWithGoogle(request)
                onLoginSuccess(response)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Google Login Failure: ${e.message}")
                errorMessage.value = "Google Login Error: ${e.message}"
            }
        }
    }

    fun retrieveOotds() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = OotdApi.service.getOotds()
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Retrieve Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveOotd(authToken: String, namaOutfit: String, deskripsi: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bearerToken = "Bearer $authToken"

                val namaOutfitBody = namaOutfit.toRequestBody("text/plain".toMediaTypeOrNull())
                val deskripsiBody = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
                val imagePart = bitmap.toMultipartBody()

                OotdApi.service.addOotd(bearerToken, namaOutfitBody, deskripsiBody, imagePart)
                retrieveOotds()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Save Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateOotd(authToken: String, ootdToUpdate: Ootd, newNamaOutfit: String, newDeskripsi: String, newBitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bearerToken = "Bearer $authToken"

                val namaOutfitBody = newNamaOutfit.toRequestBody("text/plain".toMediaTypeOrNull())
                val deskripsiBody = newDeskripsi.toRequestBody("text/plain".toMediaTypeOrNull())
                val imagePart = newBitmap?.toMultipartBody()

                OotdApi.service.updateOotd(bearerToken, ootdToUpdate.id, namaOutfitBody, deskripsiBody, imagePart)
                retrieveOotds()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Update Failure: ${e.message}")
                errorMessage.value = "Error updating: ${e.message}"
            }
        }
    }

    fun deleteOotd(authToken: String, ootdId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bearerToken = "Bearer $authToken"
                val response = OotdApi.service.deleteOotd(bearerToken, ootdId)
                if (response.isSuccessful) {
                    retrieveOotds()
                } else {
                    throw Exception("Failed to delete. Code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Delete Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    fun clearMessage() {
        errorMessage.value = null
    }

    fun getOotdById(id: String): com.selly0024.mopro1_assesment3.model.Ootd? { // Changed function name and type
        return data.value.find { it.id == id }
    }
}