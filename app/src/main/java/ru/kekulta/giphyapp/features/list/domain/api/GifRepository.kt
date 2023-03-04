package ru.kekulta.giphyapp.features.list.domain.api


import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.shared.data.models.GifSearchResponse
import ru.kekulta.giphyapp.shared.data.models.Resource

interface GifRepository {
    suspend fun searchGifs(request: GifSearchRequest): Resource<GifSearchResponse>
}