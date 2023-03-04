package ru.kekulta.giphyapp.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.shared.data.models.GifSearchResponse
import ru.kekulta.giphyapp.shared.data.models.Resource

interface GifInteractor {
    suspend fun searchGifs(request: GifSearchRequest): Flow<Resource<GifSearchResponse>>
    suspend fun likeGif(id: String)
    suspend fun unlikeGif(id: String)
}