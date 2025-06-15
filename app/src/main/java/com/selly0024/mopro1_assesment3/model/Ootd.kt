package com.selly0024.mopro1_assesment3.model


import com.squareup.moshi.Json

data class Ootd(
    val id: String,
    @Json(name = "namaOutfit")
    val namaOutfit: String,
    val deskripsi: String,
    @Json(name = "imageId")
    val imageId: String,
    val userId: Int?,
    @Json(name = "imageUrl")
    val imageUrl: String?
)
