package ru.exemple.picsumtest.data.model

data class PictureModel(
        val id: String,
        val author: String,
        val width: Int,
        val height: Int,
        val url: String,
        val download_url: String
)