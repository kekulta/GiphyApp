package ru.kekulta.giphyapp.features.list.domain.api

import ru.kekulta.giphyapp.shared.data.models.Gif

interface GifRepository {
    suspend fun searchGifs(query: String): List<Gif>
}